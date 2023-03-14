package com.coska.aws.convert;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeConverter implements DynamoDBTypeConverter<Long, ZonedDateTime> {

    @Override
    public Long convert(ZonedDateTime dt) {
        return dt.toEpochSecond();
    }

    @Override
    public ZonedDateTime unconvert(Long dt) {
        if (dt != null)
            return ZonedDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.of("UTC"));
        return null;
    }
}
