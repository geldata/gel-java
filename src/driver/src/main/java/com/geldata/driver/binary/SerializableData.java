package com.geldata.driver.binary;

import javax.naming.OperationNotSupportedException;

public interface SerializableData {
    void write(final PacketWriter writer) throws OperationNotSupportedException;
    int getSize();

}
