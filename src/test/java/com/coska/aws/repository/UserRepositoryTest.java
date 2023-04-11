package com.coska.aws.repository;

import com.coska.aws.entity.User;
import com.coska.aws.config.DynamoDBTestConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    private static final Logger logger = LogManager.getLogger(UserRepositoryTest.class);
    @Autowired
    private UserRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        logger.debug("Initialize User DataModel");
        dynamoDBTestConfiguration.dynamoDBUserSetup();

        // in case previous testing didn't clean up
        List<User> users =  repository.findAll();
        for(User user : users) {
            repository.delete(user.getId());
        }
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        logger.debug("User Item Count: " + dynamoDBTestConfiguration.getUserItemCount());
    }

    @Test
    void userCRUD() {
        logger.debug("Test userCRUD");
        
        List<User> users = new ArrayList<>();
        // Test save()
        for(int i = 0; i < 3; i++) {
            User user = User.builder().id("user" + i).build();
            user.setFirstName("FirstName" + i);
            user.setLastName("LastName" + i);
            user.setType("user");
            user.setGptKey("GptKey" + i);

            User saved = repository.save(user);
            assertNotNull(saved);
            users.add(user);
        }
        logger.debug("User Item Count: " + dynamoDBTestConfiguration.getUserItemCount());

        assertEquals(3, users.size());

        // Test get()
        for(int i = 0; i < 3; i++) {
            User found = repository.get("user" + i);
            assertNotNull(found);
            logger.debug("Get User: " + found);
        }

        // Test findAll()
        List<User> users1 =  repository.findAll();
        assertEquals(3, users1.size());
        for(User user : users1) {
            logger.debug("findAll User: " + user);
            repository.delete(user.getId());
        }
    }
}
