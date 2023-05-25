package com.edgedb.driver.binary.descriptors.common;

import com.edgedb.driver.binary.packets.shared.Cardinality;
import com.edgedb.driver.binary.PacketReader;
import org.joou.UInteger;
import org.joou.UShort;

import java.util.EnumSet;

public final class ShapeElement {
    public final EnumSet<ShapeElementFlags> flags;
    public final Cardinality cardinality;
    public final String name;
    public final UShort typePosition;

    public ShapeElement(final PacketReader reader) {
        this.flags = reader.readEnumSet(ShapeElementFlags.class, UInteger.class);
        this.cardinality = reader.readEnum(Cardinality.class, Byte.TYPE);
        this.name = reader.readString();
        this.typePosition = reader.readUInt16();
    }
}
