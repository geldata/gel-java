package com.geldata.driver.binary.codecs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.geldata.driver.binary.PacketReader;
import com.geldata.driver.binary.PacketWriter;
import com.geldata.driver.binary.codecs.scalars.TextCodec;
import com.geldata.driver.binary.protocol.common.descriptors.CodecMetadata;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class EnumerationCodec extends TextCodec {
    public final HashSet<String> members;

    public EnumerationCodec(UUID id, @Nullable CodecMetadata metadata, String[] members) {
        super(id, metadata);

        this.members = new HashSet<>(Arrays.asList(members));
    }

    @Override
    public void serialize(@NotNull PacketWriter writer, @Nullable String value, CodecContext context) throws OperationNotSupportedException {
        if(!members.contains(value)) {
            throw new IllegalArgumentException("Value is not a member of the defined enumeration");
        }

        super.serialize(writer, value, context);
    }

    @Override
    public @NotNull String deserialize(@NotNull PacketReader reader, CodecContext context) {
        var value = super.deserialize(reader, context);

        if(!members.contains(value)) {
            throw new IllegalArgumentException("Value is not a member of the defined enumeration");
        }

        return value;
    }
}
