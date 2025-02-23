package com.geldata.driver.binary.protocol.v2.descriptors;

import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class SetTypeDescriptor implements TypeDescriptor {
    public final UUID id;
    public final UShort type;

    public SetTypeDescriptor(UUID id, PacketReader reader) {
        this.id = id;
        this.type = reader.readUInt16();
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
