package com.geldata.driver.binary.protocol;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;
import java.util.UUID;

import com.geldata.driver.Capabilities;
import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.protocol.common.Cardinality;

public final class ParseResult {
    public final Codec<?> inCodec;
    public final Codec<?> outCodec;

    public final UUID inCodecId;
    public final UUID outCodecId;
    public final ByteBuf stateData;

    public final EnumSet<Capabilities> capabilities;
    public final Cardinality cardinality;


    public ParseResult(
            Codec<?> inCodec, Codec<?> outCodec, UUID inCodecId, UUID outCodecId, ByteBuf stateData, EnumSet<Capabilities> capabilities,
            Cardinality cardinality
    ) {
        this.inCodec = inCodec;
        this.outCodec = outCodec;
        this.inCodecId = inCodecId;
        this.outCodecId = outCodecId;
        this.stateData = stateData;
        this.capabilities = capabilities;
        this.cardinality = cardinality;
    }
}
