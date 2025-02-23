package com.geldata.driver.binary.codecs.scalars;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.codecs.CodecBase;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;

import java.util.UUID;

public abstract class ScalarCodecBase<T> extends CodecBase<T> implements ScalarCodec<T> {
    public ScalarCodecBase(UUID id, @Nullable CodecMetadata metadata, Class<T> cls) {
        super(id, metadata, cls);
    }
}
