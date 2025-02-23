package com.geldata.driver.binary.codecs.scalars;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.datatypes.Memory;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;

public final class MemoryCodec extends ScalarCodecBase<Memory> {
    public static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000130");

    public MemoryCodec(@Nullable CodecMetadata metadata) {
        super(ID, metadata, Memory.class);
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable Memory value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(value.getBytes());
        }
    }

    @Override
    public @NotNull Memory deserialize(@NotNull PacketReader reader, CodecContext context) {
        return new Memory(reader.readInt64());
    }
}
