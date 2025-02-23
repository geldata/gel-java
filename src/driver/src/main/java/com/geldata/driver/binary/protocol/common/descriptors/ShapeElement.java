package com.geldata.driver.binary.protocol.common.descriptors;

import org.jetbrains.annotations.NotNull;
import org.joou.UInteger;
import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.common.Cardinality;
import com.geldata.driver.binary.protocol.common.descriptors.ShapeElementFlags;

import java.util.EnumSet;

public final class ShapeElement {
    public final @NotNull EnumSet<ShapeElementFlags> flags;
    public final Cardinality cardinality;
    public final @NotNull String name;
    public final @NotNull UShort typePosition;

    public ShapeElement(final @NotNull PacketReader reader) {
        this.flags = reader.readEnumSet(ShapeElementFlags.class, UInteger.class);
        this.cardinality = reader.readEnum(Cardinality.class, Byte.TYPE);
        this.name = reader.readString();
        this.typePosition = reader.readUInt16();
    }
}
