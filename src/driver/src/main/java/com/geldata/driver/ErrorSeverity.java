package com.geldata.driver;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.BinaryEnum;

/**
 * Represents the error severity for an error returned by Gel
 * @see com.geldata.driver.exceptions.GelErrorException
 */
public enum ErrorSeverity implements BinaryEnum<Byte> {
    ERROR (0x78),
    FATAL (0xC8),
    PANIC (0xFF);

    private final byte value;

    ErrorSeverity(int value) {
        this.value = (byte)value;
    }

    @Override
    public @NotNull Byte getValue() {
        return value;
    }
}
