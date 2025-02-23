package com.geldata.driver.binary.codecs.scalars.complex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.codecs.complex.ComplexCodecConverter;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.util.TemporalUtils;

import javax.naming.OperationNotSupportedException;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

public final class DateTimeCodec extends ComplexScalarCodecBase<OffsetDateTime> {
    public static final UUID ID = UUID.fromString("00000000-0000-0000-0000-00000000010A");

    @SuppressWarnings("unchecked")
    public DateTimeCodec(@Nullable CodecMetadata metadata) {
        super(
                ID,
                metadata,
                OffsetDateTime.class,
                new ComplexCodecConverter<>(
                        ZonedDateTime.class,
                        OffsetDateTime::toZonedDateTime,
                        ZonedDateTime::toOffsetDateTime
                )
        );
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable OffsetDateTime value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(TemporalUtils.toMicrosecondsSinceEpoc(value));
        }
    }

    @Override
    public @Nullable OffsetDateTime deserialize(@NotNull PacketReader reader, CodecContext context) {
        return TemporalUtils.fromMicrosecondsSinceEpoc(reader.readInt64(), ZonedDateTime::toOffsetDateTime);
    }
}
