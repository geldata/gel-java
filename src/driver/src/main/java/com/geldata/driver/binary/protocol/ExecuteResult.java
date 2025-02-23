package com.geldata.driver.binary.protocol;

import io.netty.buffer.ByteBuf;

import java.util.List;

import com.geldata.driver.binary.codecs.Codec;

public class ExecuteResult {
    public final Codec<?> codec;
    public final List<ByteBuf> data;


    public ExecuteResult(Codec<?> codec, List<ByteBuf> data) {
        this.codec = codec;
        this.data = data;
    }
}
