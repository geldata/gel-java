package com.geldata.driver.binary.protocol;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.SerializableData;
import com.geldata.driver.binary.protocol.ClientMessageType;

import static com.geldata.driver.util.BinaryProtocolUtils.BYTE_SIZE;
import static com.geldata.driver.util.BinaryProtocolUtils.INT_SIZE;

import javax.naming.OperationNotSupportedException;

public abstract class Sendable implements SerializableData {
    public final ClientMessageType type;

    public Sendable(ClientMessageType type) {
        this.type = type;
    }

    protected abstract void buildPacket(final PacketWriter writer) throws OperationNotSupportedException;

    @Override
    public void write(final @NotNull PacketWriter writer) throws OperationNotSupportedException {
        writer.write(type.getCode());
        writer.write(getDataSize() + 4);
        buildPacket(writer);
    }

    @Override
    public int getSize() {
        return getDataSize() + BYTE_SIZE + INT_SIZE;
    }

    protected abstract int getDataSize();

}
