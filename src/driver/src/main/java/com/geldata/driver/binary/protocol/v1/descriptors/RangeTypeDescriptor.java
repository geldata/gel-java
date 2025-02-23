package com.geldata.driver.binary.protocol.v1.descriptors;

import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class RangeTypeDescriptor implements TypeDescriptor {
    public final @NotNull UShort typePosition;

    private final UUID id;

    public RangeTypeDescriptor(final UUID id, final @NotNull PacketReader reader) {
        this.id = id;
        this.typePosition = reader.readUInt16();
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
