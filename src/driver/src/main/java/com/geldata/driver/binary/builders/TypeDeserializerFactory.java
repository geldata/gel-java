package com.geldata.driver.binary.builders;

import org.jetbrains.annotations.Nullable;

import com.geldata.driver.ObjectEnumerator;
import com.geldata.driver.exceptions.GelException;

import javax.naming.OperationNotSupportedException;

@FunctionalInterface
public interface TypeDeserializerFactory<T> {
    T deserialize(
            final ObjectEnumerator enumerator,
            final @Nullable ParentDeserializer<T> parent
    ) throws GelException, OperationNotSupportedException, ReflectiveOperationException;

    default T deserialize(
            final ObjectEnumerator enumerator
    ) throws GelException, OperationNotSupportedException, ReflectiveOperationException {
        return deserialize(enumerator, null);
    }

    @FunctionalInterface
    interface ParentDeserializer<T> {
        void accept(T value, ObjectEnumerator.ObjectElement element) throws GelException, ReflectiveOperationException;
    }
}
