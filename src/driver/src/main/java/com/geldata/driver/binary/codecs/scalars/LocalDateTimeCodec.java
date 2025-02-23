package com.geldata.driver.binary.codecs.scalars;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.util.TemporalUtils;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class LocalDateTimeCodec extends ScalarCodecBase<LocalDateTime> {
    public static final UUID ID = UUID.fromString("00000000-0000-0000-0000-00000000010B");

    public LocalDateTimeCodec(@Nullable CodecMetadata metadata) {
        super(ID, metadata, LocalDateTime.class);
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable LocalDateTime value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(ChronoUnit.MICROS.between(TemporalUtils.GEL_EPOC_LOCAL, value));
        }
    }

    @Override
    public @NotNull LocalDateTime deserialize(@NotNull PacketReader reader, CodecContext context) {
        return TemporalUtils.GEL_EPOC_LOCAL.plus(reader.readInt64(), ChronoUnit.MICROS);
    }
}
