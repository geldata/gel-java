package com.geldata.driver.binary.codecs.complex;

import com.geldata.driver.binary.codecs.RuntimeCodec;

@FunctionalInterface
public interface RuntimeCodecFactory {
    RuntimeCodec<?> create(Class<?> cls, ComplexCodecBase<?> parent, ComplexCodecConverter<?, ?> converter);
}
