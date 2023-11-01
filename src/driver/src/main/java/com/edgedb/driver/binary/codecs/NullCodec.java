package com.edgedb.driver.binary.codecs;

import com.edgedb.driver.binary.PacketWriter;
import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.builders.CodecBuilder;
import com.edgedb.driver.binary.protocol.common.descriptors.CodecMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public final class NullCodec implements Codec<Void>, ArgumentCodec<Void> {
    @Override
    public UUID getId() {
        return CodecBuilder.NULL_CODEC_ID;
    }

    @Override
    public @Nullable CodecMetadata getMetadata() {
        return null;
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable Void value, CodecContext context) throws OperationNotSupportedException {
        writer.write(0);
    }

    @Nullable
    @Override
    public Void deserialize(PacketReader reader, CodecContext context) {
        return null;
    }

    @Override
    public @NotNull Class<Void> getConvertingClass() {
        return Void.class;
    }

    @Override
    public boolean canConvert(Type type) {
        return true;
    }

    @Override
    public void serializeArguments(PacketWriter writer, @Nullable Map<String, ?> value, CodecContext context) {}
}
