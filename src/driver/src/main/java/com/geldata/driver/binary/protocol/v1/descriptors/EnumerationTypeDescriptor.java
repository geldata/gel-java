package com.geldata.driver.binary.protocol.v1.descriptors;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class EnumerationTypeDescriptor implements TypeDescriptor {
    public final String @NotNull [] members;

    private final UUID id;

    public EnumerationTypeDescriptor(final UUID id, final @NotNull PacketReader reader) {
        this.id = id;

        this.members = reader.readArrayOf(String.class, PacketReader::readString, Short.TYPE);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
