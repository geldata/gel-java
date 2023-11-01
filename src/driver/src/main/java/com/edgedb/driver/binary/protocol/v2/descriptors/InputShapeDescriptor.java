package com.edgedb.driver.binary.protocol.v2.descriptors;

import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.protocol.TypeDescriptor;
import com.edgedb.driver.binary.protocol.common.descriptors.ShapeElement;
import org.joou.UShort;

import java.util.UUID;

public class InputShapeDescriptor implements TypeDescriptor {
    public final UUID id;
    public final ShapeElement[] elements;

    public InputShapeDescriptor(UUID id, PacketReader reader) {
        this.id = id;
        elements = reader.readArrayOf(ShapeElement.class, ShapeElement::new, UShort.class);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
