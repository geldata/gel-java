package com.geldata.driver.binary.codecs.scalars;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.CodecContext;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;
import com.geldata.driver.util.BinaryProtocolUtils;

import javax.naming.OperationNotSupportedException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class DurationCodec extends ScalarCodecBase<Duration> {
    public static final UUID ID = UUID.fromString("00000000-0000-0000-0000-00000000010E");
    public DurationCodec(@Nullable CodecMetadata metadata) {
        super(ID, metadata, Duration.class);
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable Duration value, CodecContext context) throws OperationNotSupportedException {
        if(value != null) {
            writer.write(Math.round(value.toNanos() / 1000d));

            // deprecated: days & months
            writer.write(0);
            writer.write(0);
        }
    }

    @Override
    public @Nullable Duration deserialize(@NotNull PacketReader reader, CodecContext context) {
        var duration = Duration.of(reader.readInt64(), ChronoUnit.MICROS);

        // deprecated: days & months
        reader.skip(BinaryProtocolUtils.LONG_SIZE);

        return duration;
    }
}
