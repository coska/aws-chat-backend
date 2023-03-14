package com.coska.aws.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.coska.aws.entity.User;
import com.coska.aws.entity.Room;
import com.coska.aws.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DynamoDBTestConfiguration {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void dynamoDBUserSetup() {
        try {
            ListTablesResult tablesResult = amazonDynamoDB.listTables();
            System.out.println("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("User")) {
                System.out.println("Create User Table");
                dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
                CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(User.class);
                tableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

                amazonDynamoDB.createTable(tableRequest);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getUserItemCount() {
        return amazonDynamoDB.describeTable("User").getTable().getItemCount();
    }

    public void dynamoDBRoomSetup() {
        try {
            ListTablesResult tablesResult = amazonDynamoDB.listTables();
            System.out.println("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("Room")) {
                System.out.println("Create Room Table");
                CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Room.class);
                tableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

                amazonDynamoDB.createTable(tableRequest);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getRoomItemCount() {
        return amazonDynamoDB.describeTable("Room").getTable().getItemCount();
    }

    public void dynamoDBMessageSetup() {
        try {
            ListTablesResult tablesResult = amazonDynamoDB.listTables();
            System.out.println("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("Message")) {
                System.out.println("Create Message Table");
                dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
                CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Message.class);
                tableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

                tableRequest.getGlobalSecondaryIndexes().forEach(gsi -> {
                    gsi.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
                    // "Date" is a required attribute for the access pattern using this Global Secondary Index
                    // ProjectionType.KEYS_ONLY would miss that attribute
                    // ProjectionType.ALL would work but less efficient than ProjectionType.INCLUDE
                    gsi.getProjection().setNonKeyAttributes(List.of("Date"));
                    gsi.getProjection().setProjectionType(ProjectionType.INCLUDE);
                });

                amazonDynamoDB.createTable(tableRequest);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getMessageItemCount() {
        return amazonDynamoDB.describeTable("Message").getTable().getItemCount();
    }
}
