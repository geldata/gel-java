package com.geldata.driver.binary.protocol.common.descriptors;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;

public final class TupleElement {
    public final @NotNull String name;
    public final short typePosition;

    public TupleElement(final @NotNull PacketReader reader) {
        this.name = reader.readString();
        this.typePosition = reader.readInt16();
    }
}
