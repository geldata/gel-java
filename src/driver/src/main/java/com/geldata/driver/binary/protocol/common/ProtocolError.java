package com.geldata.driver.binary.protocol.common;

import org.joou.UShort;

import com.geldata.driver.ErrorCode;
import com.geldata.driver.ErrorSeverity;

import java.util.Optional;

public interface ProtocolError {
    ErrorSeverity getSeverity();
    ErrorCode getErrorCode();
    String getMessage();

    Optional<KeyValue> tryGetAttribute(short code);
}
