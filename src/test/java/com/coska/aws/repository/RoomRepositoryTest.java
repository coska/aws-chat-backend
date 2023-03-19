package com.coska.aws.repository;

import com.coska.aws.config.DynamoDBTestConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RoomRepositoryTest {
    private static final Logger logger = LogManager.getLogger(RoomRepositoryTest.class);
//    @Autowired
//    private RoomRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        logger.debug("Initialize Room DataModel");
        dynamoDBTestConfiguration.dynamoDBRoomSetup();

        // in case previous testing didn't clean up
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        logger.debug("Room Item Count: " + dynamoDBTestConfiguration.getRoomItemCount());
    }

    @Test
    void roomCRUD() {
        System.out.println("roomCRUD");
    }
}
