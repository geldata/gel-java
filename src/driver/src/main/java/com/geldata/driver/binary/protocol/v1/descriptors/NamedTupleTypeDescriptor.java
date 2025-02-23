package com.geldata.driver.binary.protocol.v1.descriptors;

import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.TypeDescriptor;
import com.geldata.driver.binary.protocol.common.descriptors.TupleElement;

import java.util.UUID;

public final class NamedTupleTypeDescriptor implements TypeDescriptor {
    public final TupleElement @NotNull [] elements;

    private final UUID id;

    public NamedTupleTypeDescriptor(final UUID id, final @NotNull PacketReader reader) {
        this.id = id;
        this.elements = reader.readArrayOf(TupleElement.class, TupleElement::new, UShort.class);
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
