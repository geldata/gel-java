package com.geldata.driver.binary.protocol;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.GelConnection;
import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.protocol.v1.V1ProtocolProvider;
import com.geldata.driver.binary.protocol.v2.V2ProtocolProvider;
import com.geldata.driver.clients.GelBinaryClient;
import com.geldata.driver.exceptions.MissingCodecException;
import com.geldata.driver.exceptions.UnexpectedMessageException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public interface ProtocolProvider {
    @Nullable ProtocolProvider DEFAULT_PROVIDER = null;

    ConcurrentMap<GelConnection, Function<GelBinaryClient, ProtocolProvider>> PROVIDERS_FACTORY = new ConcurrentHashMap<>();
    Map<ProtocolVersion, Function<GelBinaryClient, ProtocolProvider>> PROVIDERS = new HashMap<>(){{
       put(ProtocolVersion.of(1, 0), V1ProtocolProvider::new);
       put(ProtocolVersion.of(2, 0), V2ProtocolProvider::new);
    }};

    static ProtocolProvider getProvider(GelBinaryClient client) {
        return PROVIDERS_FACTORY.computeIfAbsent(
                client.getConnectionArguments(),
                ignored -> PROVIDERS.get(ProtocolVersion.BINARY_PROTOCOL_DEFAULT_VERSION)
        ).apply(client);
    }

    static void updateProviderFor(GelBinaryClient client, ProtocolProvider provider) {
        PROVIDERS_FACTORY.put(client.getConnectionArguments(), PROVIDERS.get(provider.getVersion()));
    }


    ProtocolVersion getVersion();
    ProtocolPhase getPhase();
    Map<String, @Nullable Object> getServerConfig();

    Receivable readPacket(ServerMessageType type, int length, PacketReader reader) throws UnexpectedMessageException;
    TypeDescriptorInfo<? extends Enum<?>> readDescriptor(PacketReader reader) throws UnexpectedMessageException;
    <T extends Enum<T>> @Nullable Codec<?> buildCodec(
            TypeDescriptorInfo<T> descriptor,
            Function<Integer, Codec<?>> getRelativeCodec, Function<Integer, TypeDescriptorInfo<?>> getRelativeDescriptor
    ) throws MissingCodecException;


    CompletionStage<ParseResult> parseQuery(QueryParameters queryParameters);
    CompletionStage<ExecuteResult> executeQuery(QueryParameters queryParameters, ParseResult parseResult);

    CompletionStage<Void> sendSyncMessage();
    CompletionStage<Void> processMessage(Receivable packet);

    Sendable handshake();
    Sendable terminate();
    Sendable sync();
}
