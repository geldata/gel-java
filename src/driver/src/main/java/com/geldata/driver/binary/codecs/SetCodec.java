package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;

import static com.geldata.driver.util.BinaryProtocolUtils.INT_SIZE;
import static com.geldata.driver.util.BinaryProtocolUtils.LONG_SIZE;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public final class SetCodec<T> extends CodecBase<Collection<T>> {
    private final Codec<T> innerCodec;

    @SuppressWarnings("unchecked")
    public SetCodec(UUID id, @Nullable CodecMetadata metadata, Class<?> cls, Codec<?> innerCodec) {
        super(id, metadata, (Class<Collection<T>>) cls);
        this.innerCodec = (Codec<T>) innerCodec;
    }

    @Override
    public void serialize(PacketWriter writer, @Nullable Collection<T> value, CodecContext context) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public @NotNull Collection<T> deserialize(@NotNull PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        return deserializeSet(
                reader,
                context,
                innerCodec instanceof ArrayCodec<?>
                        ? this::deserializeEnvelopeElement
                        : this::deserializeSetElement
        );
    }

    @SuppressWarnings("unchecked")
    private @NotNull Collection<T> deserializeSet(@NotNull PacketReader reader, CodecContext context, @NotNull SetElementDeserializer<T> elementDeserializer) throws GelException, OperationNotSupportedException {
        var dimensions = reader.readInt32();

        reader.skip(LONG_SIZE); // flags & reserved

        if(dimensions == 0) {
            return Set.of();
        }

        if(dimensions != 1) {
            throw new GelException("Only dimensions of 1 are supported for sets");
        }

        var upper = reader.readInt32();
        var lower = reader.readInt32();

        var numElements = upper - lower + 1;

        var result = (T[])Array.newInstance(innerCodec.getConvertingClass(), numElements);

        for(int i = 0; i != numElements; i++) {
            result[i] = elementDeserializer.deserialize(reader, context);
        }

        return Arrays.asList(result);
    }

    private @Nullable T deserializeEnvelopeElement(@NotNull PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        try(var elementReader = reader.scopedSlice()) {
            var envelopeElements = elementReader.readInt32();

            if(envelopeElements != 1) {
                throw new GelException(String.format("Envelope should contain only one element, but this envelope contains %d", envelopeElements));
            }

            elementReader.skip(INT_SIZE);

            return innerCodec.deserialize(elementReader, context);
        }
    }

    private @Nullable T deserializeSetElement(@NotNull PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        try(var elementReader = reader.scopedSlice()) {
            if(elementReader.isNoData) {
                return null;
            }

            return innerCodec.deserialize(elementReader, context);
        }
    }

    @FunctionalInterface
    private interface SetElementDeserializer<T> {
        @Nullable T deserialize(PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException;
    }
}
