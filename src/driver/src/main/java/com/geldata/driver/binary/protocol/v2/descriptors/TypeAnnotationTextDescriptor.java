package com.geldata.driver.binary.protocol.v2.descriptors;

import org.joou.UShort;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.builders.CodecBuilder;
import com.geldata.driver.binary.protocol.TypeDescriptor;

import java.util.UUID;

public final class TypeAnnotationTextDescriptor implements TypeDescriptor {
    public final UUID id;
    public final UShort descriptor;
    public final String key;
    public final String value;

    public TypeAnnotationTextDescriptor(PacketReader reader) {
        this.id = CodecBuilder.NULL_CODEC_ID;
        this.descriptor = reader.readUInt16();
        this.key = reader.readString();
        this.value = reader.readString();
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
