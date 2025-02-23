package com.geldata.driver.binary.protocol.v1.receivables;

import org.jetbrains.annotations.NotNull;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.protocol.Receivable;
import com.geldata.driver.binary.protocol.ServerMessageType;
import com.geldata.driver.binary.protocol.common.KeyValue;

public class DumpBlock implements Receivable {
    public final KeyValue @NotNull [] attributes;

    public DumpBlock(@NotNull PacketReader reader) {
        attributes = reader.readAttributes();
    }

    @Override
    public void close() throws Exception {
        release(attributes);
    }

    @Override
    public @NotNull ServerMessageType getMessageType() {
        return ServerMessageType.DUMP_BLOCK;
    }
}
