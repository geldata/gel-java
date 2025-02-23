package com.geldata.driver.binary.protocol.v1.receivables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;

public class ServerKeyData implements Receivable {
    public static final int SERVER_KEY_LENGTH = 32;

    public final ByteBuf keyData;

    public ServerKeyData(@NotNull PacketReader reader) {
        keyData = reader.readBytes(SERVER_KEY_LENGTH);
    }

    @Override
    public void close() {
        if(keyData != null) {
            keyData.release();
        }
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.SERVER_KEY_DATA;
    }
}
