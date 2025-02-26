package com.geldata.driver.binary.protocol.common;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.BinaryEnum;

public enum Cardinality implements BinaryEnum<Byte> {
    NO_RESULT    (0x6e),
    AT_MOST_ONE  (0x6f),
    ONE          (0x41),
    MANY         (0x6d),
    AT_LEAST_ONE (0x4d);


    private final byte value;

    Cardinality(int value) {
        this.value = (byte)value;
    }

    @Override
    public @NotNull Byte getValue() {
        return this.value;
    }
}
