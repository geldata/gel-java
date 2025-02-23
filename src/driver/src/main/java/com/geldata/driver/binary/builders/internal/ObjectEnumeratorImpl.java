package com.geldata.driver.binary.builders.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.ObjectEnumerator;
import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.codecs.ObjectCodec;
import com.geldata.driver.clients.GelBinaryClient;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;

import static com.geldata.driver.util.BinaryProtocolUtils.INT_SIZE;

import java.util.HashMap;
import java.util.Map;

public final class ObjectEnumeratorImpl implements ObjectEnumerator {
    private final @NotNull PacketReader reader;
    private final CodecContext context;
    private final ObjectCodec codec;

    private final int numElements;
    private int position;

    public ObjectEnumeratorImpl(@NotNull PacketReader reader, ObjectCodec codec, CodecContext context) {
        this.reader = reader;
        this.context = context;
        this.codec = codec;

        this.numElements = reader.readInt32();
    }

    public GelBinaryClient getClient() {
        return this.context.client;
    }

    @Override
    public boolean hasRemaining() {
        return position < numElements && !reader.isEmpty();
    }

    @Override
    public @Nullable ObjectEnumerator.ObjectElement next() throws GelException, OperationNotSupportedException {
        if(!hasRemaining()) {
            return null;
        }

        try {
            reader.skip(INT_SIZE);

            var element = codec.elements[position];

            try(var elementReader = reader.scopedSlice()) {
                if(elementReader.isNoData) {
                    return new ObjectEnumerator.ObjectElement(element.name, null, element.codec.getConvertingClass());
                }

                return new ObjectEnumerator.ObjectElement(
                        element.name,
                        element.codec.deserialize(elementReader, context),
                        element.codec.getConvertingClass()
                );
            }
        }
        finally {
            position++;
        }
    }

    @Override
    public @NotNull Map<String, Object> flatten() throws GelException, OperationNotSupportedException {
        return new HashMap<>() {
            {
                ObjectEnumerator.ObjectElement element;
                while(hasRemaining() && (element = next()) != null) {
                    put(element.getName(), element.getValue());
                }
            }
        };
    }
}