package com.geldata.driver.binary.protocol.v1.sendables;

import org.jetbrains.annotations.NotNull;
import org.joou.UShort;

import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.protocol.ClientMessageType;
import com.geldata.driver.binary.protocol.Sendable;
import com.geldata.driver.binary.protocol.common.ConnectionParam;
import com.geldata.driver.binary.protocol.common.ProtocolExtension;
import com.geldata.driver.util.BinaryProtocolUtils;

import javax.naming.OperationNotSupportedException;

public class ClientHandshake extends Sendable {
    private final UShort majorVersion;
    private final UShort minorVersion;
    private final ConnectionParam[] connectionParams;
    private final ProtocolExtension[] extensions;

    public ClientHandshake(
            UShort majorVersion,
            UShort minorVersion,
            ConnectionParam[] connectionParams,
            ProtocolExtension[] extensions
    ) {
        super(ClientMessageType.CLIENT_HANDSHAKE);
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.connectionParams = connectionParams;
        this.extensions = extensions;
    }

    @Override
    protected void buildPacket(@NotNull PacketWriter writer) throws OperationNotSupportedException {
        writer.write(this.majorVersion);
        writer.write(this.minorVersion);
        writer.writeArray(this.connectionParams, Short.TYPE);
        writer.writeArray(this.extensions, Short.TYPE);
    }

    @Override
    public int getDataSize() {
        return
                BinaryProtocolUtils.SHORT_SIZE +
                BinaryProtocolUtils.SHORT_SIZE +
                BinaryProtocolUtils.sizeOf(connectionParams, Short.TYPE) +
                BinaryProtocolUtils.sizeOf(extensions, Short.TYPE);
    }
}
