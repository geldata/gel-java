package com.geldata.driver;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.BinaryEnum;

/**
 * Represents the message severity within a log message sent by the server.
 */
public enum LogSeverity implements BinaryEnum<Byte> {
    DEBUG   (0x14),
    INFO    (0x28),
    NOTICE  (0x3C),
    WARNING (0x50);

    private final byte value;

    LogSeverity(int value){
        this.value = (byte)value;
    }

    @Override
    public @NotNull Byte getValue() {
        return this.value;
    }
}
