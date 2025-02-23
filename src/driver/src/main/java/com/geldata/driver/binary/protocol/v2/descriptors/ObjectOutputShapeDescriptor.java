package com.geldata.driver.binary.protocol.v2.descriptors;

import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class ObjectOutputShapeDescriptor implements TypeDescriptor {
    public final UUID id;
    public final boolean isEphemeralFreeShape;
    public final UShort type;
    public final ShapeElement[] elements;

    public ObjectOutputShapeDescriptor(UUID id, PacketReader reader) {
        this.id = id;
        this.isEphemeralFreeShape = reader.readBoolean();
        this.type = reader.readUInt16();
        this.elements = reader.readArrayOf(ShapeElement.class, ShapeElement::new, UShort.class);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
