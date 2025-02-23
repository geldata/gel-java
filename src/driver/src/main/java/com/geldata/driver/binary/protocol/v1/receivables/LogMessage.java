package com.geldata.driver.binary.protocol.v1.receivables;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.ErrorCode;
import com.geldata.driver.LogSeverity;
import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;
import com.geldata.driver.binary.protocol.common.Annotation;

public class LogMessage implements Receivable {
    public final LogSeverity severity;
    public final ErrorCode code;
    public final @NotNull String content;
    public final Annotation @NotNull [] annotations;


    public LogMessage(@NotNull PacketReader reader) {
        severity = reader.readEnum(LogSeverity.class, Byte.TYPE);
        code = reader.readEnum(ErrorCode.class, Integer.TYPE);
        content = reader.readString();
        annotations = reader.readAnnotations();
    }

    public String format() {
        return String.format("%s: %s", severity, content);
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.LOG_MESSAGE;
    }
}
