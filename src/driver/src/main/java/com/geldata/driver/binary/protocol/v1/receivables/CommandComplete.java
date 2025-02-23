package com.geldata.driver.binary.protocol.v1.receivables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.Capabilities;
import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;
import com.geldata.driver.binary.protocol.common.Annotation;

import java.util.EnumSet;
import java.util.UUID;

public class CommandComplete implements Receivable {
    public final @NotNull EnumSet<Capabilities> capabilities;
    public final @NotNull UUID stateTypeDescriptorId;
    public final @NotNull String status;
    public final @Nullable ByteBuf stateData;
    public final Annotation @NotNull [] annotations;

    public CommandComplete(@NotNull PacketReader reader) {
        this.annotations = reader.readAnnotations();
        this.capabilities = reader.readEnumSet(Capabilities.class, Long.TYPE);
        this.status = reader.readString();
        this.stateTypeDescriptorId = reader.readUUID();
        this.stateData = reader.readByteArray();
    }

    @Override
    public void close() {
        if(stateData != null) {
            stateData.release();
        }
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.COMMAND_COMPLETE;
    }
}
