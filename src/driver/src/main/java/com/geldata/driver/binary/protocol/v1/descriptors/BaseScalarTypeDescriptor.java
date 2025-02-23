package com.geldata.driver.binary.protocol.v1.descriptors;

import java.util.UUID;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

public final class BaseScalarTypeDescriptor implements TypeDescriptor {
    private final UUID id;

    public BaseScalarTypeDescriptor(final UUID id, final PacketReader ignored) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
