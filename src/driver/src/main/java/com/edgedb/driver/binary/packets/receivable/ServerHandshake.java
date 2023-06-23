package com.edgedb.driver.binary.packets.receivable;

import com.edgedb.driver.binary.packets.shared.ProtocolExtension;
import com.edgedb.driver.binary.PacketReader;
import com.edgedb.driver.binary.packets.ServerMessageType;
import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

public class ServerHandshake implements Receivable {
    public final @NotNull UShort majorVersion;
    public final @NotNull UShort minorVersion;
    public final ProtocolExtension @NotNull [] extensions;

    public ServerHandshake(@NotNull PacketReader reader) {
        majorVersion = reader.readUInt16();
        minorVersion = reader.readUInt16();
        extensions = reader.readArrayOf(ProtocolExtension.class, ProtocolExtension::new, Short.TYPE);
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.SERVER_HANDSHAKE;
    }
}
