package com.geldata.driver.binary.protocol.v2.descriptors;

import org.joou.UInteger;
import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.common.Cardinality;
import com.geldata.driver.binary.protocol.common.descriptors.ShapeElementFlags;

public final class ShapeElement {
    public final ShapeElementFlags flags;
    public final Cardinality cardinality;
    public final String name;
    public final UShort typePosition;
    public final UShort sourceTypePosition;

    public ShapeElement(PacketReader reader) {
        this.flags = reader.readEnum(ShapeElementFlags.class, UInteger.class);
        this.cardinality = reader.readEnum(Cardinality.class, Byte.TYPE);
        this.name = reader.readString();
        this.typePosition = reader.readUInt16();
        this.sourceTypePosition = reader.readUInt16();
    }
}
