package com.coska.aws.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Coska.AWS.Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @DynamoDBHashKey
    private String email;
    private String firstName;
    private String lastName;

}
