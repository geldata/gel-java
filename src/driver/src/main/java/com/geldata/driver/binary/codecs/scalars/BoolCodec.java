package com.geldata.driver.binary.codecs.scalars;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;

public final class BoolCodec extends ScalarCodecBase<Boolean> {
    public static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000109");
    public BoolCodec(@Nullable CodecMetadata metadata) {
        super(ID, metadata, Boolean.class);
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable Boolean value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(value);
        }
    }

    @Override
    public @NotNull Boolean deserialize(@NotNull PacketReader reader, CodecContext context) {
        return reader.readBoolean();
    }
}
