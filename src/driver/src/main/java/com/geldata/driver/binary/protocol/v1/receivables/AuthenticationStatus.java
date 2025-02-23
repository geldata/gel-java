package com.geldata.driver.binary.protocol.v1.receivables;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;
import com.geldata.driver.binary.protocol.common.AuthStatus;

public class AuthenticationStatus implements Receivable {
    public final AuthStatus authStatus;
    @Nullable
    public final String @Nullable [] authenticationMethods;
    @Nullable
    public final ByteBuf saslData;

    public AuthenticationStatus(@NotNull PacketReader reader) {
        this.authStatus = reader.readEnum(AuthStatus.class, Integer.TYPE);

        switch (this.authStatus) {
            case AUTHENTICATION_REQUIRED_SASL_MESSAGE:
                authenticationMethods = reader.readStringArray();
                saslData = null;
                break;
            case AUTHENTICATION_SASL_CONTINUE:
            case AUTHENTICATION_SASL_FINAL:
                saslData = reader.readByteArray();
                authenticationMethods = null;
                break;
            default:
                authenticationMethods = null;
                saslData = null;

        }
    }

    @Override
    public void close() {
        if(saslData != null) {
            saslData.release();
        }
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.AUTHENTICATION;
    }
}
