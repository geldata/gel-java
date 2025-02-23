package com.geldata.driver.binary.protocol.v1.descriptors;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class ScalarTypeNameAnnotation implements TypeDescriptor {
    public final @NotNull String name;

    private final UUID id;

    public ScalarTypeNameAnnotation(final UUID id, final @NotNull PacketReader reader) {
        this.id = id;
        this.name = reader.readString();
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
