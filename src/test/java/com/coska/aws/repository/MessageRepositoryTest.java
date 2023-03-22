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
public class MessageRepositoryTest {
    private static final Logger logger = LogManager.getLogger(MessageRepositoryTest.class);

//    @Autowired
//    private MessageRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        System.out.println("Initialize Message DataModel");
        dynamoDBTestConfiguration.dynamoDBMessageSetup();

        // in case previous testing didn't clean up
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        logger.debug("Message Item Count: " + dynamoDBTestConfiguration.getMessageItemCount());
    }

    @Test
    void messageCRUD() {
        System.out.println("messageCRUD");
    }
}
