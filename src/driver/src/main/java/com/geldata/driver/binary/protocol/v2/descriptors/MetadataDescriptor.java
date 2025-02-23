package com.geldata.driver.binary.protocol.v2.descriptors;

import org.jetbrains.annotations.Nullable;
import org.joou.UShort;

import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.protocol.TypeDescriptorInfo;
import com.geldata.driver.binary.protocol.common.descriptors.CodecAncestor;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;

import java.util.function.Function;

public interface MetadataDescriptor {
    static CodecAncestor[] constructAncestors(
            UShort[] ancestors,
            Function<Integer, Codec<?>> getRelativeCodec,
            Function<Integer, TypeDescriptorInfo<?>> getRelativeDescriptor
    ) {
        var codecAncestors = new CodecAncestor[ancestors.length];

        for(var i = 0; i != ancestors.length; i++) {
            codecAncestors[i] = new CodecAncestor(
                    getRelativeCodec.apply(ancestors[i].intValue()),
                    getRelativeDescriptor.apply(ancestors[i].intValue())
            );
        }

        return codecAncestors;
    }

    @Nullable CodecMetadata getMetadata(
            Function<Integer, Codec<?>> getRelativeCodec,
            Function<Integer, TypeDescriptorInfo<?>> getRelativeDescriptor
    );
}
