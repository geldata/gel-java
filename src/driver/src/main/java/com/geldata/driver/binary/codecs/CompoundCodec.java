package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.binary.protocol.common.descriptors.TypeOperation;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;

import static com.geldata.driver.util.BinaryProtocolUtils.INT_SIZE;

import java.util.Collection;
import java.util.UUID;

public class CompoundCodec extends CodecBase<Object> {
    private final TypeOperation operation;
    private final Codec<?>[] innerCodecs;

    public CompoundCodec(
            UUID id,
            @Nullable CodecMetadata metadata,
            TypeOperation operation,
            Codec<?>[] elements
    ) {
        super(id, metadata, Object.class);

        this.operation = operation;
        this.innerCodecs = elements;
    }

    public TypeOperation getTypeOperation() {
        return this.operation;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void serialize(PacketWriter writer, @Nullable Object value, CodecContext context) throws OperationNotSupportedException, GelException {
        if(value == null) {
            writer.write(-1);
            return;
        }

        if(!(value instanceof Collection<?>)) {
            throw new IllegalArgumentException("The provided argument was not a collection");
        }


        var collection = ((Collection<?>)value).toArray();

        writer.write(collection.length);

        var visitor = context.getTypeVisitor();

        for(int i = 0; i != collection.length; i++) {
            writer.write(0); // reserved

            var elementValue = collection[i];

            if(elementValue == null) {
                writer.write(-1);
                continue;
            }

            visitor.setTargetType(elementValue.getClass());
            Codec codec = visitor.visit(this.innerCodecs[i]);
            visitor.reset();

            writer.writeDelegateWithLength((v) -> codec.serialize(v, elementValue, context));
        }
    }

    @Nullable
    @Override
    public Object deserialize(PacketReader reader, CodecContext context) throws GelException, OperationNotSupportedException {
        var numElements = reader.readInt32();

        if(numElements != this.innerCodecs.length) {
            throw new GelException("Expected " + this.innerCodecs.length + " elements, but got " + numElements);
        }

        var elements = new Object[numElements];

        for(var i = 0; i != numElements; i++) {
            reader.skip(INT_SIZE);

            try(var elementReader = reader.scopedSlice()) {
                elements[i] = innerCodecs[i].deserialize(elementReader, context);
            }
        }

        return elements;
    }
}
