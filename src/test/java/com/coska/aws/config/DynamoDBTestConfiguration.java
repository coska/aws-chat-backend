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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DynamoDBTestConfiguration {
    private static final Logger logger = LogManager.getLogger(DynamoDBTestConfiguration.class);

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void dynamoDBUserSetup() {
        try {
            ListTablesResult tablesResult = amazonDynamoDB.listTables();
            logger.debug("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("User")) {
                logger.debug("Create User Table");
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
            logger.debug("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("Room")) {
                logger.debug("Create Room Table");
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
            logger.debug("TableNames: " + tablesResult.getTableNames());
            if (!tablesResult.getTableNames().contains("Message")) {
                logger.debug("Create Message Table");
                dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
                CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Message.class);
                tableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

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
