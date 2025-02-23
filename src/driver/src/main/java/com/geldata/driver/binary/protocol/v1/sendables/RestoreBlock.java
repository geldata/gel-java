package com.geldata.driver.binary.protocol.v1.sendables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.ClientMessageType;
import com.geldata.driver.binary.protocol.Sendable;
import com.geldata.driver.util.BinaryProtocolUtils;

import javax.naming.OperationNotSupportedException;

public class RestoreBlock extends Sendable {
    private final ByteBuf blockData;

    public RestoreBlock(ByteBuf blockData) {
        super(ClientMessageType.RESTORE_BLOCK);
        this.blockData = blockData;
    }

    @Override
    public int getDataSize() {
        return BinaryProtocolUtils.sizeOf(blockData);
    }

    @Override
    protected void buildPacket(@NotNull PacketWriter writer) throws OperationNotSupportedException {
        writer.writeArray(blockData);
    }
}
