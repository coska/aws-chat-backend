package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.coska.aws.convert.ZonedDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "Message")
@NoArgsConstructor
@SuppressWarnings("unused")
public class Message {
    // PK
    @DynamoDBRangeKey(attributeName = "id")
    private String id;

    @DynamoDBHashKey(attributeName = "roomId")
    private String roomId;

    @DynamoDBAttribute(attributeName = "senderId")
    private String senderId;

    @DynamoDBAttribute(attributeName = "payload")
    private String payload;

    @DynamoDBAttribute(attributeName = "type")
    private String type;

    @DynamoDBAttribute(attributeName = "timestamp")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private ZonedDateTime timestamp;

//    @DynamoDBIndexHashKey(attributeName = "GSI_1_PK", globalSecondaryIndexName = "GSI_1")
//    public String getGSI1PK() {
//        return roomId;
//    }
//
//    public void setGSI1PK(String pk) {
//        // intentionally left blank
//    }
//
//    @DynamoDBIndexRangeKey( attributeName = "GSI_1_SK", globalSecondaryIndexName = "GSI_1")
//    public String getGSI1SK() {
//        return id;
//    }
//
//    public void setGSI1SK(String sk) {
//        // intentionally left blank
//    }
}