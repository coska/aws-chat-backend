package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "User")
@NoArgsConstructor
@SuppressWarnings("unused")
public class User {
    // PK
    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "gptKey")
    private String gptKey;

    @DynamoDBAttribute(attributeName = "type")
    private String type;
}