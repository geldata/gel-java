package com.geldata.driver.binary.protocol.v1.sendables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.ClientMessageType;
import com.geldata.driver.binary.protocol.Sendable;
import com.geldata.driver.binary.protocol.common.KeyValue;
import com.geldata.driver.util.BinaryProtocolUtils;

import javax.naming.OperationNotSupportedException;

public class Restore extends Sendable {
    private final KeyValue[] attributes;
    private final short jobs;
    private final ByteBuf headerData;

    public Restore(KeyValue[] attributes, short jobs, ByteBuf headerData) {
        super(ClientMessageType.RESTORE);
        this.attributes = attributes;
        this.jobs = jobs;
        this.headerData = headerData;
    }

    @Override
    public int getDataSize() {
        return BinaryProtocolUtils.SHORT_SIZE + BinaryProtocolUtils.sizeOf(attributes, Short.TYPE) + BinaryProtocolUtils.sizeOf(headerData);
    }

    @Override
    protected void buildPacket(@NotNull PacketWriter writer) throws OperationNotSupportedException {
        writer.writeArray(attributes, Short.TYPE);
        writer.write(jobs);
        writer.writeArray(headerData);
    }
}
