package com.geldata.driver.binary.protocol.common.descriptors;

import com.geldata.driver.binary.BinaryEnum;

public enum TypeOperation implements BinaryEnum<Byte> {
    UNION(1),
    INTERSECTION(2);

    private final byte value;

    TypeOperation(int value) {
        this.value = (byte)(value & 0xFF);
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}
