package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.builders.internal.ObjectEnumeratorImpl;
import com.geldata.driver.binary.builders.types.TypeBuilder;
import com.geldata.driver.binary.builders.types.TypeDeserializerInfo;
import com.geldata.driver.binary.protocol.common.Cardinality;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.exceptions.GelException;
import com.geldata.driver.exceptions.NoTypeConverterException;

import javax.naming.OperationNotSupportedException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("rawtypes")
public class ObjectCodec extends CodecBase<Object> implements ArgumentCodec<Object> {
    public static ObjectProperty propertyOf(String name, Cardinality cardinality, Codec<?> codec) {
        return new ObjectProperty(name, codec, cardinality);
    }

    public static final class TypeInitializedObjectCodec extends ObjectCodec {
        private final @Nullable TypeDeserializerInfo<?> deserializer;
        private final Class<?> target;
        private final @NotNull ObjectCodec parent;

        public TypeInitializedObjectCodec(@NotNull Class<?> target, @NotNull ObjectCodec parent) throws GelException {
            super(parent);

            this.parent = parent;
            this.target = target;
            this.deserializer = TypeBuilder.getDeserializerInfo(target);

            if(this.deserializer == null) {
                throw new NoTypeConverterException("Failed to find type deserializer for " + target.getName());
            }
        }

        public TypeInitializedObjectCodec(@NotNull TypeDeserializerInfo<?> info, @NotNull ObjectCodec parent) {
            super(parent);

            this.parent = parent;
            this.target = info.getType();
            this.deserializer = info;
        }

        @Override
        public @Nullable Object deserialize(@NotNull PacketReader reader, CodecContext context) throws GelException {
            assert deserializer != null;

            var enumerator = new ObjectEnumeratorImpl(reader, this, context);

            try {
                return deserializer.factory.deserialize(enumerator);
            } catch (Exception x) {
                throw new GelException("Failed to deserialize " + target.getName(), x);
            }
        }

        public Class<?> getTarget() {
            return target;
        }

        public @NotNull ObjectCodec getParent() {
            return parent;
        }

        public @Nullable TypeDeserializerInfo<?> getDeserializer() {
            return deserializer;
        }
    }

    public static final class ObjectProperty {
        public final String name;
        public final @Nullable Cardinality cardinality;
        public Codec<?> codec;
        public ObjectProperty(String name, Codec<?> codec, @Nullable Cardinality cardinality) {
            this.name = name;
            this.codec = codec;
            this.cardinality = cardinality;
        }
    }

    public final @Nullable UUID typeId;
    public final ObjectProperty[] elements;
    private final @NotNull ConcurrentMap<Class<?>, TypeInitializedObjectCodec> typeCodecs;

    public ObjectCodec(UUID shapeId, @Nullable UUID typeId, @Nullable CodecMetadata metadata, ObjectProperty... elements) {
        super(shapeId, metadata, Object.class);
        this.typeId = typeId;
        this.elements = elements;
        this.typeCodecs = new ConcurrentHashMap<>();
    }

    private ObjectCodec(ObjectCodec other) {
        super(other.id, other.metadata, Object.class);
        this.typeId = other.typeId;
        this.elements = other.elements;
        this.typeCodecs = other.typeCodecs;
    }

    public TypeInitializedObjectCodec getOrCreateTypeCodec(Class<?> cls) throws GelException {
        return getOrCreateTypeCodec(cls, t -> new TypeInitializedObjectCodec(t, this));
    }
    public TypeInitializedObjectCodec getOrCreateTypeCodec(@NotNull TypeDeserializerInfo<?> info) throws GelException {
        return getOrCreateTypeCodec(info.getType(), t -> new TypeInitializedObjectCodec(info, this));
    }

    @FunctionalInterface
    private interface TypeInitializedCodecFactory {
        TypeInitializedObjectCodec construct(Class<?> cls) throws GelException;
    }
    private TypeInitializedObjectCodec getOrCreateTypeCodec(Class<?> cls, @NotNull TypeInitializedCodecFactory factory) throws GelException {
        try {
            return typeCodecs.computeIfAbsent(cls, t -> {
                try {
                    return factory.construct(t);
                } catch (GelException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (RuntimeException e) {
            if(e.getCause() instanceof GelException) {
                throw (GelException)e.getCause();
            }

            throw e;
        }
    }

    @Override
    public final void serializeArguments(@NotNull PacketWriter writer, @Nullable Map<String, ?> value, @NotNull CodecContext context) throws GelException, OperationNotSupportedException {
        this.serialize(writer, value, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void serialize(@NotNull PacketWriter writer, @Nullable Object rawValue, @NotNull CodecContext context) throws OperationNotSupportedException, GelException {
        if(rawValue == null) {
            throw new IllegalArgumentException("Serializable object value cannot be null");
        }

        if(!(rawValue instanceof Map)) {
            throw new GelException("Expected map type for object serialization");
        }

        var value = (Map<String, ?>)rawValue;

        writer.write(value.size());

        var visitor = context.getTypeVisitor();

        for(int i = 0; i != value.size(); i++) {
            var element = this.elements[i];

            writer.write(0); // reserved

            if(!value.containsKey(element.name)) {
                writer.write(-1);
                continue;
            }

            var elementValue = value.get(element.name);

            if(elementValue == null) {
                writer.write(-1);
                continue;
            }

            visitor.setTargetType(elementValue.getClass());
            var codec = (Codec)visitor.visit(element.codec);
            visitor.reset();

            writer.writeDelegateWithLength((v) -> codec.serialize(v, elementValue, context));
        }
    }

    @Override
    public @Nullable Object deserialize(@NotNull PacketReader reader, CodecContext context) throws GelException {
        var enumerator = new ObjectEnumeratorImpl(reader, this, context);

        try {
            return enumerator.flatten();
        } catch (Exception x) {
            throw new GelException("Failed to deserialize object to " + getConvertingClass().getName(), x);
        }
    }
}
