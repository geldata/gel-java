package com.geldata.driver.binary.codecs.scalars.complex;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.codecs.ComplexCodec;
import com.geldata.driver.binary.codecs.RuntimeCodec;
import com.geldata.driver.binary.codecs.complex.ComplexCodecBase;
import com.geldata.driver.binary.codecs.complex.ComplexCodecConverter;
import com.geldata.driver.binary.codecs.scalars.ScalarCodec;
import com.geldata.driver.binary.codecs.scalars.ScalarCodecBase;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;

public abstract class ComplexScalarCodecBase<T> extends ComplexCodecBase<T> implements ScalarCodec<T> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ComplexScalarCodecBase(UUID id, @Nullable CodecMetadata metadata, Class<T> cls, ComplexCodecConverter<T, ?>... converters) {
        super(id, metadata, cls, (c, p, cv) -> new RuntimeScalarCodecImpl(c, p, cv), converters);
    }

}

final class RuntimeScalarCodecImpl<T, U> extends ScalarCodecBase<U> implements RuntimeCodec<U> {
    private final ComplexCodecBase<T> parent;
    private final ComplexCodecConverter<T, U> converter;


    public RuntimeScalarCodecImpl(Class<U> cls, ComplexCodecBase<T> parent, ComplexCodecConverter<T, U> converter) {
        super(parent.id, parent.metadata, cls);
        this.parent = parent;
        this.converter = converter;
    }

    @Override
    public void serialize(PacketWriter writer, @Nullable U value, CodecContext context) throws OperationNotSupportedException, GelException {
        var converted = value == null ? null : converter.from.apply(value);
        this.parent.serialize(writer, converted, context);
    }

    @Override
    public @Nullable U deserialize(PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        var value = parent.deserialize(reader, context);
        return value == null ? null : converter.to.apply(value);
    }

    @Override
    public ComplexCodec<?> getBroker() {
        return this.parent;
    }
}
