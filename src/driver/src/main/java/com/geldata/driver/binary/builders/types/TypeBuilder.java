package com.geldata.driver.binary.builders.types;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.annotations.GelType;
import com.geldata.driver.binary.codecs.Codec;
import com.geldata.driver.binary.codecs.ObjectCodec;
import com.geldata.driver.clients.GelBinaryClient;
import com.geldata.driver.datatypes.Tuple;
import com.geldata.driver.datatypes.internal.TupleImpl;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class TypeBuilder {
    private static final @NotNull ConcurrentMap<Class<?>, TypeDeserializerInfo<?>> deserializerInfo;


    static {
        deserializerInfo = new ConcurrentHashMap<>() {{
            put(Map.class, new TypeDeserializerInfo<>(Map.class, (e, v) -> e.flatten()));
            put(Object.class, new TypeDeserializerInfo<>(Object.class, (e, v) -> e.flatten()));
            put(Tuple.class, new TypeDeserializerInfo<>(TupleImpl.class));
        }};
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T buildObject(@NotNull GelBinaryClient client, @NotNull Class<T> type, ObjectCodec codec, @NotNull ByteBuf data) throws OperationNotSupportedException, GelException {
        var info = getDeserializerInfo(type);

        if(info == null) {
            throw new OperationNotSupportedException("Cannot deserialize object data to " + type.getName());
        }

        if(!(codec instanceof ObjectCodec.TypeInitializedObjectCodec)) {
            codec = codec.getOrCreateTypeCodec(info);
        }


        return (T) Codec.deserializeFromBuffer(codec, data, client.getCodecContext());
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable TypeDeserializerInfo<T> getDeserializerInfo(@NotNull Class<T> cls) {
        if(!isValidObjectType(cls)) {
            return null;
        }

        var info = (TypeDeserializerInfo<T>) deserializerInfo.computeIfAbsent(cls, TypeDeserializerInfo::new);

        info.scanChildren();

        return info;
    }

    public static boolean requiredImplicitTypeNames(@NotNull Class<?> cls) {
        var info = getDeserializerInfo(cls);
        return info != null && info.requiresTypeNameIntrospection();
    }

    private static boolean isValidObjectType(@NotNull Class<?> type) {
        if(deserializerInfo.containsKey(type)) {
            return true;
        }

        return type.getAnnotation(GelType.class) != null;
    }
}
