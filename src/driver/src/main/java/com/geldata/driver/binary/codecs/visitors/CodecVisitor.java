package com.geldata.driver.binary.codecs.visitors;

import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.exceptions.GelException;

@FunctionalInterface
public interface CodecVisitor {
    Codec<?> visit(Codec<?> codec) throws GelException;
}
