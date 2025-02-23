package com.geldata.driver.binary.protocol.v1.descriptors;

import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;
import com.geldata.driver.binary.protocol.common.descriptors.ShapeElement;

import java.util.UUID;

public class InputShapeDescriptor implements TypeDescriptor {
    public final ShapeElement @NotNull [] shapes;

    private final UUID id;

    public InputShapeDescriptor(final UUID id, final @NotNull PacketReader reader) {
        this.id = id;
        this.shapes = reader.readArrayOf(ShapeElement.class, ShapeElement::new, UShort.class);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
