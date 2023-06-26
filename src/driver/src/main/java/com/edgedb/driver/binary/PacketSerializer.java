package com.edgedb.driver.binary;

import com.edgedb.driver.binary.packets.ServerMessageType;
import com.edgedb.driver.binary.packets.receivable.*;
import com.edgedb.driver.binary.packets.sendables.Sendable;
import com.edgedb.driver.exceptions.ConnectionFailedException;
import com.edgedb.driver.exceptions.EdgeDBException;
import com.edgedb.driver.util.HexUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.edgedb.driver.util.BinaryProtocolUtils.BYTE_SIZE;
import static com.edgedb.driver.util.BinaryProtocolUtils.INT_SIZE;

public class PacketSerializer {
    private static final Logger logger = LoggerFactory.getLogger(PacketSerializer.class);
    private static final @NotNull Map<ServerMessageType, Function<PacketReader, Receivable>> deserializerMap;
    private static final Map<Class<?>, Map<Number, Enum<?>>> binaryEnumMap = new HashMap<>();

    static {
        deserializerMap = new HashMap<>();

        deserializerMap.put(ServerMessageType.AUTHENTICATION, AuthenticationStatus::new);
        deserializerMap.put(ServerMessageType.COMMAND_COMPLETE, CommandComplete::new);
        deserializerMap.put(ServerMessageType.COMMAND_DATA_DESCRIPTION, CommandDataDescription::new);
        deserializerMap.put(ServerMessageType.DATA, Data::new);
        deserializerMap.put(ServerMessageType.DUMP_BLOCK, DumpBlock::new);
        deserializerMap.put(ServerMessageType.DUMP_HEADER, DumpHeader::new);
        deserializerMap.put(ServerMessageType.ERROR_RESPONSE, ErrorResponse::new);
        deserializerMap.put(ServerMessageType.LOG_MESSAGE, LogMessage::new);
        deserializerMap.put(ServerMessageType.PARAMETER_STATUS, ParameterStatus::new);
        deserializerMap.put(ServerMessageType.READY_FOR_COMMAND, ReadyForCommand::new);
        deserializerMap.put(ServerMessageType.RESTORE_READY, RestoreReady::new);
        deserializerMap.put(ServerMessageType.SERVER_HANDSHAKE, ServerHandshake::new);
        deserializerMap.put(ServerMessageType.SERVER_KEY_DATA, ServerKeyData::new);
        deserializerMap.put(ServerMessageType.STATE_DATA_DESCRIPTION, StateDataDescription::new);
    }

    public static <T extends Enum<?> & BinaryEnum<U>, U extends Number> void registerBinaryEnum(Class<T> cls, T @NotNull [] values) {
        binaryEnumMap.put(cls, Arrays.stream(values).collect(Collectors.toMap(BinaryEnum::getValue, v -> v)));
    }

    public static <T extends Enum<T> & BinaryEnum<U>, U extends Number> T getEnumValue(@NotNull Class<T> enumCls, U raw) {
        if(!binaryEnumMap.containsKey(enumCls)) {
            registerBinaryEnum(enumCls, enumCls.getEnumConstants());
        }

        //noinspection unchecked
        return (T)binaryEnumMap.get(enumCls).get(raw);
    }

    public static @NotNull MessageToMessageDecoder<ByteBuf> createDecoder() {
        return new MessageToMessageDecoder<>() {
            private final Map<Channel, PacketContract> contracts = new HashMap<>();

            @Override
            protected void decode(@NotNull ChannelHandlerContext ctx, @NotNull ByteBuf msg, @NotNull List<Object> out) throws Exception {
                while (msg.readableBytes() > 5) {
                    var type = getEnumValue(ServerMessageType.class, msg.readByte());
                    var length = msg.readUnsignedInt() - 4; // remove length of self.

                    // can we read this packet?
                    if (msg.readableBytes() >= length) {
                        var packet = PacketSerializer.deserialize(type, length, msg.readSlice((int) length));
                        logger.debug("S->C: T:{}", type);
                        out.add(packet);
                        continue;
                    }

                    if (contracts.containsKey(ctx.channel())) {
                        var contract = contracts.get(ctx.channel());

                        if (contract.tryComplete(msg)) {
                            out.add(contract.getPacket());
                        }

                        return;
                    } else {
                        contracts.put(ctx.channel(), new PacketContract(msg, type, length));
                    }
                }

                if (msg.readableBytes() > 0) {
                    if (contracts.containsKey(ctx.channel())) {
                        var contract = contracts.get(ctx.channel());

                        if (contract.tryComplete(msg)) {
                            out.add(contract.getPacket());
                        }
                    } else {
                        contracts.put(ctx.channel(), new PacketContract(msg, null, null));
                    }
                }
            }

            class PacketContract {
                private @Nullable Receivable packet;
                private ByteBuf data;

                private @Nullable ServerMessageType messageType;
                private @Nullable Long length;

                public PacketContract(
                        ByteBuf data,
                        @Nullable ServerMessageType messageType,
                        @Nullable Long length
                ) {
                    this.data = data;
                    this.length = length;
                    this.messageType = messageType;
                }

                public boolean tryComplete(@NotNull ByteBuf other) {
                    if (messageType == null) {
                        messageType = pick(other, b -> getEnumValue(ServerMessageType.class, b.readByte()), BYTE_SIZE);
                    }

                    if (length == null) {
                        length = pick(other, b -> b.readUnsignedInt() - 4, INT_SIZE);
                    }

                    data = Unpooled.wrappedBuffer(data, other);

                    if (data.readableBytes() >= length) {
                        // read
                        packet = PacketSerializer.deserialize(messageType, length, data);
                        return true;
                    }

                    return false;
                }

                private <T> T pick(@NotNull ByteBuf other, @NotNull Function<ByteBuf, T> map, long sz) {
                    if (data.readableBytes() > sz) {
                        return map.apply(data);
                    } else if (other.readableBytes() < sz) {
                        throw new IndexOutOfBoundsException();
                    }

                    return map.apply(other);
                }

                public @NotNull Receivable getPacket() throws OperationNotSupportedException {
                    if (packet == null) {
                        throw new OperationNotSupportedException("Packet contract was incomplete");
                    }

                    return packet;
                }
            }
        };
    }

    public static @NotNull MessageToMessageEncoder<Sendable> createEncoder() {
        return new MessageToMessageEncoder<>() {

            @Override
            protected void encode(@NotNull ChannelHandlerContext ctx, @NotNull Sendable msg, @NotNull List<Object> out) {

                try {
                    var data = PacketSerializer.serialize(msg);

                    data.readerIndex(0);

                    logger.debug("C->S: T:{} D:{}", msg.type, HexUtils.bufferToHexString(data));

                    out.add(data);
                } catch (Throwable x) {
                    logger.error("Failed to serialize packet", x);
                    ctx.fireExceptionCaught(x);
                    ctx.fireUserEventTriggered("DISCONNECT");
                }
            }
        };
    }

    public static @Nullable Receivable deserialize(ServerMessageType messageType, long length, @NotNull ByteBuf buffer) {
        var reader = new PacketReader(buffer);
        return deserializeSingle(messageType, length, reader, true);
    }

    public static @Nullable Receivable deserializeSingle(PacketReader reader) {
        var messageType = reader.readEnum(ServerMessageType.class, Byte.TYPE);
        var length = reader.readUInt32().longValue();

        return deserializeSingle(messageType, length, reader, false);
    }

    public static @Nullable Receivable deserializeSingle(
            ServerMessageType type, long length, @NotNull PacketReader reader,
            boolean verifyEmpty
    ) {
        if(!deserializerMap.containsKey(type)) {
            logger.error("Unknown packet type {}", type);
            reader.skip(length);
            return null;
        }

        try {
            return deserializerMap.get(type).apply(reader);
        }
        catch (Exception x) {
            logger.error("Failed to deserialize packet", x);
            throw x;
        }
        finally {
            // ensure we read the entire packet
            if(verifyEmpty && !reader.isEmpty()) {
                logger.warn("Hanging data left inside packet reader of type {} with length {}", type, length);
            }
        }
    }

    public static HttpResponse.BodyHandler<List<Receivable>> PACKET_BODY_HANDLER = new PacketBodyHandler();

    private static class PacketBodyHandler implements HttpResponse.BodyHandler<List<Receivable>> {
        @Override
        public HttpResponse.BodySubscriber<List<Receivable>> apply(HttpResponse.ResponseInfo responseInfo) {
            // ensure success
            var isSuccess = responseInfo.statusCode() / 100 == 2;

            return isSuccess
                    ? new PacketBodySubscriber()
                    : new PacketBodySubscriber(responseInfo.statusCode());
        }

        private static class PacketBodySubscriber implements HttpResponse.BodySubscriber<List<Receivable>> {
            private final @Nullable List<@NotNull ByteBuf> buffers;
            private final CompletableFuture<List<Receivable>> promise;

            public PacketBodySubscriber(int errorCode) {
                buffers = null;
                promise = CompletableFuture.failedFuture(
                        new ConnectionFailedException("Got HTTP error code " + errorCode)
                );
            }

            public PacketBodySubscriber() {
                promise = new CompletableFuture<>();
                buffers = new ArrayList<>();
            }

            @Override
            public CompletionStage<List<Receivable>> getBody() {
                return promise;
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                if(buffers == null) {
                    return; // failed
                }

                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<ByteBuffer> items) {
                if(buffers == null) {
                    return; // failed
                }

                for(var item : items) {
                    buffers.add(Unpooled.wrappedBuffer(item));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                promise.completeExceptionally(throwable);
            }

            @Override
            public void onComplete() {
                if(buffers == null) {
                    return; // failed
                }

                var completeBuffer = Unpooled.wrappedBuffer(buffers.toArray(new ByteBuf[0]));

                var reader = new PacketReader(completeBuffer);
                var data = new ArrayList<Receivable>();

                while(completeBuffer.readableBytes() > 0) {
                    var packet = deserializeSingle(reader);

                    if(packet == null && completeBuffer.readableBytes() > 0) {
                        promise.completeExceptionally(
                                new EdgeDBException("Failed to deserialize packet, buffer had " + completeBuffer.readableBytes() + " bytes remaining")
                        );
                        return;
                    }

                    data.add(packet);
                }

                promise.complete(data);
            }
        }
    }

    public static ByteBuf serialize(@NotNull Sendable packet, @Nullable Sendable @Nullable ... packets) throws OperationNotSupportedException {
        int size = packet.getSize();

        if(packets != null && packets.length > 0) {
            size += Arrays.stream(packets)
                    .filter(Objects::nonNull)
                    .mapToInt(Sendable::getSize)
                    .sum();
        }

        try (var writer = new PacketWriter(size)) {
            packet.write(writer);

            if(packets != null) {
                for (var p : packets) {
                    assert p != null;
                    p.write(writer);
                }
            }

            return writer.getBuffer();
        }
    }
}
