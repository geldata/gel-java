package com.geldata.driver.binary.protocol.v1.receivables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;

public class ParameterStatus implements Receivable {
    public final @NotNull String name;
    public final @Nullable ByteBuf value;

    public ParameterStatus(@NotNull PacketReader reader) {
        name = reader.readString();
        value = reader.readByteArray();
    }

    @Override
    public void close() {
        if(value != null) {
            value.release();
        }
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.PARAMETER_STATUS;
    }
}
