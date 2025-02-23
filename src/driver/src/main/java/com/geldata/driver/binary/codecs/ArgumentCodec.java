package com.geldata.driver.binary.codecs;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;
import java.util.Map;

public interface ArgumentCodec<T> extends Codec<T> {
    void serializeArguments(final PacketWriter writer, @Nullable Map<String, ?> value, CodecContext context) throws GelException, OperationNotSupportedException;
    static <T> ByteBuf serializeToBuffer(@NotNull ArgumentCodec<T> codec, final @Nullable Map<String, ?> value, final CodecContext context) throws OperationNotSupportedException, GelException {
        try (var writer = new PacketWriter()) {
            codec.serializeArguments(writer, value, context);
            return writer.getBuffer();
        }
    }
}
