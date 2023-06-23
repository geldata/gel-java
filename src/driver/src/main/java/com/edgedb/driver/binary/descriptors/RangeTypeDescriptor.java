package com.edgedb.driver.binary.descriptors;

import com.edgedb.driver.binary.PacketReader;
import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

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
