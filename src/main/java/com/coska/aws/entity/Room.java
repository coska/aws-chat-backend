package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "Room")
@NoArgsConstructor
@SuppressWarnings("unused")
public class Room {
    // PK
    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBAttribute(attributeName = "title")
    private String title;

    @DynamoDBAttribute(attributeName = "lastMessage")
    private String lastMessage;

    @DynamoDBAttribute(attributeName = "lastSentUserld")
    private String lastSentUserId;

    @DynamoDBAttribute(attributeName = "participants")
    public List<String> getParticipants() {
        return participants;
    }
    private List<String> participants;
}