package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;

@DynamoDBTable(tableName = "Coska.AWS.Employee")
@Data
public class Employee {

    @DynamoDBHashKey
    private String email;
    private String firstName;
    private String lastName;

}
