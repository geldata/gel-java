package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.datatypes.MultiRange;
import com.geldata.driver.datatypes.Range;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;
import java.util.UUID;

public final class MultiRangeCodec<T> extends CodecBase<MultiRange<T>> {
    private final RangeCodec<T> rangeCodec;

    @SuppressWarnings("unchecked")
    public MultiRangeCodec(UUID id, @Nullable CodecMetadata metadata, Class<?> cls, Codec<T> elementCodec) {
        super(id, metadata, (Class<MultiRange<T>>)cls);
        this.rangeCodec = new RangeCodec<>(id, metadata, cls, elementCodec);
    }

    @Override
    public void serialize(PacketWriter writer, @Nullable MultiRange<T> value, CodecContext context) throws OperationNotSupportedException, GelException {
        if(value == null) {
            return;
        }

        writer.write(value.length);

        for(int i = 0; i != value.length; i++) {
            var element = value.get(i);
            writer.writeDelegateWithLength(w -> rangeCodec.serialize(w, element, context));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable MultiRange<T> deserialize(PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        var length = reader.readInt32();

        if(length == 0) {
            return MultiRange.empty();
        }

        var elements = new Range[length];

        for(int i = 0; i != length; i++) {
            try(var scoped = reader.scopedSlice(reader.readInt32())) {
                elements[i] = rangeCodec.deserialize(scoped, context);
            }
        }

        return new MultiRange<T>(elements);
    }
}
