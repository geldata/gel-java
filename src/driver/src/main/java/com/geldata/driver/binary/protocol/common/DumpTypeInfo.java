package com.geldata.driver.binary.protocol.common;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;

import java.util.UUID;

public class DumpTypeInfo {
    public final @NotNull String name;
    public final @NotNull String className;
    public final @NotNull UUID id;

    public DumpTypeInfo(@NotNull PacketReader reader) {
        name = reader.readString();
        className = reader.readString();
        id = reader.readUUID();
    }

}
