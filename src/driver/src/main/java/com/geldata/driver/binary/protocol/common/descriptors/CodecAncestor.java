package com.geldata.driver.binary.protocol.common.descriptors;

import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.protocol.TypeDescriptorInfo;

public final class CodecAncestor {
    public final Codec<?> codec;
    public final TypeDescriptorInfo<?> descriptor;

    public CodecAncestor(Codec<?> codec, TypeDescriptorInfo<?> descriptor) {
        this.codec = codec;
        this.descriptor = descriptor;
    }
}
