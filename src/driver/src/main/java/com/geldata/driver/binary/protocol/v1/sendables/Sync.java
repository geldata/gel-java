package com.geldata.driver.binary.protocol.v1.sendables;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.ClientMessageType;
import com.geldata.driver.binary.protocol.Sendable;

public class Sync extends Sendable {
    public static final Sendable INSTANCE = new Sync();

    public Sync() {
        super(ClientMessageType.SYNC);
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    protected void buildPacket(PacketWriter writer) { /* no data */ }
}
