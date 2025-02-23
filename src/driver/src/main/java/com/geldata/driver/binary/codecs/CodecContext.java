package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.codecs.visitors.TypeVisitor;
import com.geldata.driver.clients.GelBinaryClient;

public final class CodecContext {
    public final GelBinaryClient client;

    public CodecContext(GelBinaryClient client) {
        this.client = client;
    }

    public @NotNull TypeVisitor getTypeVisitor() {
        return new TypeVisitor(this.client);
    }
}
