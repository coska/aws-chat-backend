package com.coska.aws.repository;

import com.coska.aws.config.DynamoDBTestConfiguration;
import com.coska.aws.entity.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class MessageRepositoryTest {
    private static final Logger logger = LogManager.getLogger(MessageRepositoryTest.class);

    @Autowired
    private MessageRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        logger.debug("Initialize Message DataModel");
        dynamoDBTestConfiguration.dynamoDBMessageSetup();

        // in case previous testing didn't clean up
        List<Message> messages =  repository.findAll();
        for(Message message : messages) {
            repository.delete(message.getId());
        }
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        logger.debug("Message Item Count: " + dynamoDBTestConfiguration.getMessageItemCount());
    }

    @Test
    void messageCRUD() {
        logger.debug("messageCRUD");

        List<Message> messages = new ArrayList<>();
        // Test save()
        for(int i = 0; i < 3; i++) {
            Message message = Message.builder().id("Message" + i).build();
            message.setRoomId("RoomId" + i);
            message.setSenderId("SenderId" + i);
            message.setPayload("Payload");
            message.setType("Type");
            message.setTimestamp(ZonedDateTime.now(ZoneId.of("America/Toronto")));

            Message saved = repository.save(message);
            assertNotNull(saved);
            messages.add(message);
        }
        logger.debug("Message Item Count: " + dynamoDBTestConfiguration.getMessageItemCount());

        assertEquals(3, messages.size());

        // Test get()
        for(int i = 0; i < 3; i++) {
            Message found = repository.get("Message" + i);
            assertNotNull(found);
            logger.debug("Get Message: " + found);
        }

        // Test findAll()
        List<Message> messages1 =  repository.findAll();
        assertEquals(3, messages1.size());
        for(Message message : messages1) {
            logger.debug("findAll Message: " + message);
            repository.delete(message.getId());
        }
    }
}
