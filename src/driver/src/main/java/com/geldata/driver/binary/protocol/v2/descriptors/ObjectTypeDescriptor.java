package com.geldata.driver.binary.protocol.v2.descriptors;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.protocol.TypeDescriptor;
import com.geldata.driver.binary.protocol.TypeDescriptorInfo;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;

import java.util.UUID;
import java.util.function.Function;

public final class ObjectTypeDescriptor implements TypeDescriptor, MetadataDescriptor {
    public final UUID id;
    public final String name;
    public final boolean isSchemaDefined;

    public ObjectTypeDescriptor(UUID id, PacketReader reader) {
        this.id = id;
        this.name = reader.readString();
        this.isSchemaDefined = reader.readBoolean();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public @NotNull CodecMetadata getMetadata(
            Function<Integer, Codec<?>> getRelativeCodec,
            Function<Integer, TypeDescriptorInfo<?>> getRelativeDescriptor
    ) {
        return new CodecMetadata(
                name,
                isSchemaDefined
        );
    }
}
