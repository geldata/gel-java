package com.geldata.driver.binary.protocol.v1.sendables;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.ClientMessageType;
import com.geldata.driver.binary.protocol.Sendable;
import com.geldata.driver.binary.protocol.common.Annotation;
import com.geldata.driver.util.BinaryProtocolUtils;

import javax.naming.OperationNotSupportedException;

public class Dump extends Sendable {
    private final Annotation[] annotations;

    public Dump(Annotation[] annotations) {
        super(ClientMessageType.DUMP);
        this.annotations = annotations;
    }

    @Override
    public int getDataSize() {
        return BinaryProtocolUtils.sizeOf(this.annotations, Short.TYPE);
    }

    @Override
    protected void buildPacket(@NotNull PacketWriter writer) throws OperationNotSupportedException {
        writer.writeArray(this.annotations, Short.TYPE);
    }
}
