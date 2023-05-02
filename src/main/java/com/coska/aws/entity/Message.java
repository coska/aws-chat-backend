package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.coska.aws.convert.ZonedDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "Message")
@NoArgsConstructor
@SuppressWarnings("unused")
public class Message {
    // PK
    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "payload")
    private String payload;

    @DynamoDBAttribute(attributeName = "type")
    private String type;

    // GSI
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "roomIdIndex", attributeName = "roomId")
    private String roomId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "senderIdIndex", attributeName = "senderId")
    private String senderId;
    
    @DynamoDBAttribute(attributeName = "senderName")
    private String senderName;

    @DynamoDBAttribute(attributeName = "timestamp")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private ZonedDateTime timestamp;
}