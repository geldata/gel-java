package com.edgedb.driver.binary.codecs.visitors;

import com.edgedb.driver.binary.builders.types.TypeDeserializerInfo;
import com.edgedb.driver.binary.codecs.*;
import com.edgedb.driver.clients.EdgeDBBinaryClient;
import com.edgedb.driver.exceptions.EdgeDBException;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public final class TypeVisitor implements CodecVisitor {
    private static final Map<Class<? extends Codec>, TypeCodecVisitor> visitors;
    private final Stack<TypeResultContextFrame> frames;
    private final FrameHandle handle;
    private final EdgeDBBinaryClient client;

    static {
        visitors = new HashMap<>() {
            {
                put(ObjectCodec.class,     (v, c) -> visitObjectCodec(v, (ObjectCodec) c));
                put(CompilableCodec.class, (v, c) -> visitCompilableCodec(v, (CompilableCodec) c));
                put(ComplexCodec.class,    (v, c) -> visitComplexCodec(v, (ComplexCodec<?>) c));
                put(RuntimeCodec.class,    (v, c) -> visitRuntimeCodec(v, (RuntimeCodec<?>) c));
            }
        };
    }
    public TypeVisitor(EdgeDBBinaryClient client) {
        this.frames = new Stack<>();
        this.client = client;
        this.handle = new FrameHandle(this.frames::pop);
    }

    public void setTargetType(Class<?> type) {
        this.frames.push(new TypeResultContextFrame(type, false));
    }

    public void reset(){
        this.frames.clear();
    }

    @Override
    public Codec<?> visit(Codec<?> codec) throws EdgeDBException {
        if (getContext().type.equals(Void.class)) {
            return codec;
        }

        if(visitors.containsKey(codec.getClass())) {
            var visitor = visitors.get(codec.getClass());
            return visitor.visit(this, codec);
        }

        return codec;
    }

    public static Codec<?> visitObjectCodec(TypeVisitor visitor, ObjectCodec codec) throws EdgeDBException {
        codec.initialize(visitor.getContext().type);

        var info = codec.getDeserializerInfo();

        if(info == null) {
            throw new EdgeDBException("Could not find a valid deserialization strategy for " + visitor.getContext().type);
        }

        var map = info.getFieldMap(visitor.client.getConfig().getNamingStrategy());

        for(int i = 0; i != codec.elements.length; i++) {
            var element = codec.elements[i];

            TypeDeserializerInfo.FieldInfo field = null;
            Class<?> type;

            if(map.contains(element.name) && (field = map.get(element.name)) != null) {
                type = field.getType(element.cardinality);
            }
            else {
                type = element.codec instanceof CompilableCodec
                        ? ((CompilableCodec)element.codec).getInnerType()
                        : element.codec.getConvertingClass();
            }

            final var isReal = field != null;

            try(var ignored = visitor.enterNewContext(v -> {
                v.type = type;
                v.isRealType = isReal;
            })) {
                element.codec = visitor.visit(element.codec);
            }
        }

        return codec;
    }

    public static Codec<?> visitCompilableCodec(TypeVisitor visitor, CompilableCodec codec) throws EdgeDBException {
        // visit the inner codec
        Codec compiledCodec;
        try(var handle = visitor.enterNewContext(v -> {
            v.type = visitor.getContext().isRealType
                    ? visitor.getContext().type
                    : codec.getInnerType();
        })) {
            compiledCodec = codec.compile(visitor.getContext().type, visitor.visit(codec.getInnerCodec()));
        }

        return visitor.visit(compiledCodec);
    }

    public static Codec<?> visitComplexCodec(TypeVisitor visitor, ComplexCodec<?> codec) {
        return codec.getCodecFor(visitor.getContext().type);
    }

    public static Codec<?> visitRuntimeCodec(TypeVisitor visitor, RuntimeCodec<?> codec) {
        if(!visitor.getContext().type.equals(codec.getConvertingClass())) {
            return codec.getBroker().getCodecFor(visitor.getContext().type);
        }
        return codec;
    }

    private TypeResultContextFrame getContext() {
        return this.frames.peek();
    }

    private FrameHandle enterNewContext(
            Consumer<TypeResultContextFrame> func
    ) {
        var ctx = frames.empty() ? new TypeResultContextFrame(null, false) : frames.peek().clone();
        func.accept(ctx);
        frames.push(ctx);
        return this.handle;
    }

    private static final class TypeResultContextFrame implements Cloneable {
        public Class<?> type;
        public boolean isRealType;

        private TypeResultContextFrame(Class<?> type, boolean isRealType) {
            this.type = type;
            this.isRealType = isRealType;
        }

        public TypeResultContextFrame clone() {
            try {
                return (TypeResultContextFrame)super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final class FrameHandle implements Closeable {
        private final Supplier<?> free;

        public FrameHandle(Supplier<?> free) {
            this.free = free;
        }

        @Override
        public void close() {
            this.free.get();
        }
    }

    @FunctionalInterface
    private interface TypeCodecVisitor {
        Codec<?> visit(TypeVisitor visitor, Codec<?> codec) throws EdgeDBException ;
    }
}
