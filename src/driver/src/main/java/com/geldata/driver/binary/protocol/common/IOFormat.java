package com.geldata.driver.binary.protocol.common;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.BinaryEnum;

public enum IOFormat implements BinaryEnum<Byte> {
    BINARY        (0x62),
    JSON          (0x6a),
    JSON_ELEMENTS (0x4a),
    NONE          (0x6e);

    private final byte value;

    IOFormat(int value) {
        this.value = (byte)value;
    }

    @Override
    public @NotNull Byte getValue() {
        return this.value;
    }
}
