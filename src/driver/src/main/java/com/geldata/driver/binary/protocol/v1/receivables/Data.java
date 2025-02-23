package com.geldata.driver.binary.protocol.v1.receivables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;

public class Data implements Receivable {
    public final @Nullable ByteBuf payloadBuffer;

    public Data(@NotNull PacketReader reader) {
        var numElements = reader.readInt16();

        if(numElements != 1) {
            throw new IndexOutOfBoundsException("Expected one element array for data, got " + numElements);
        }

        payloadBuffer = reader.readByteArray();
    }

    @Override
    public void close() {
        if(payloadBuffer != null) {
            payloadBuffer.release();
        }
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.DATA;
    }
}
