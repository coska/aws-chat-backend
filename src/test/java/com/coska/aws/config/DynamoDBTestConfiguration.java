package com.coska.aws.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.model.*;
import com.coska.aws.entity.User;
import com.coska.aws.entity.Room;
import com.coska.aws.entity.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
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

                List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
                attributeDefinitions.add(new AttributeDefinition("id", ScalarAttributeType.S));
                attributeDefinitions.add(new AttributeDefinition("roomId", ScalarAttributeType.S));
                attributeDefinitions.add(new AttributeDefinition("senderId", ScalarAttributeType.S));

                List<KeySchemaElement> keySchema = new ArrayList<>();
                keySchema.add(new KeySchemaElement("id", KeyType.HASH));

                final ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(5L, 5L);

                CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Message.class)
                        .withAttributeDefinitions(attributeDefinitions)
                        .withKeySchema(keySchema)
                        .withProvisionedThroughput(provisionedThroughput);
                // At this moment, Message.class already defined globalSecondaryIndexes.
                // So, we do not need to add globalSecondaryIndexes
                logger.debug(" Secondary Index size(0) =  " + createTableRequest.getGlobalSecondaryIndexes().size());
                for ( GlobalSecondaryIndex secIdx : createTableRequest.getGlobalSecondaryIndexes()) {
                	logger.debug(" secIdx.name = " + secIdx.getIndexName());
                 	secIdx.setProvisionedThroughput(provisionedThroughput);
                }                
                /*
                GlobalSecondaryIndex roomIdIndex = new GlobalSecondaryIndex()
                        .withIndexName("roomIdIndex")
                        .withKeySchema(new KeySchemaElement("roomId", KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(provisionedThroughput);

                GlobalSecondaryIndex senderIdIndex = new GlobalSecondaryIndex()
                        .withIndexName("senderIdIndex")
                        .withKeySchema(new KeySchemaElement("senderId", KeyType.HASH))
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(provisionedThroughput);

                createTableRequest.withGlobalSecondaryIndexes(roomIdIndex, senderIdIndex);

                for ( GlobalSecondaryIndex secIdx : createTableRequest.getGlobalSecondaryIndexes()) {
                	logger.debug(" secIdx.name = " + secIdx.getIndexName());
                	
                	secIdx.setProvisionedThroughput(provisionedThroughput);
                }
                */
               
                TableDescription tableDesc = amazonDynamoDB.createTable(createTableRequest).getTableDescription();

                while (!tableDesc.getTableStatus().equals(TableStatus.ACTIVE.toString())) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tableDesc = amazonDynamoDB.describeTable("Message").getTable();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getMessageItemCount() {
        return amazonDynamoDB.describeTable("Message").getTable().getItemCount();
    }
}
