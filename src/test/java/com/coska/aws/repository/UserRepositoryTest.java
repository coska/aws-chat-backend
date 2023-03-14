package com.coska.aws.repository;

import com.coska.aws.entity.User;
import com.coska.aws.config.DynamoDBTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        System.out.println("Initialize User DataModel");
        dynamoDBTestConfiguration.dynamoDBUserSetup();

        // in case previous testing didn't clean up
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        System.out.println("User Item Count: " + dynamoDBTestConfiguration.getUserItemCount());
    }

    @Test
    void userCRUD() {
        System.out.println("Test userCRUD");

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
        System.out.println("User Item Count: " + dynamoDBTestConfiguration.getUserItemCount());

        assertEquals(3, users.size());

        // Test get()
        for(int i = 0; i < 3; i++) {
            User found = repository.get("user" + i);
            assertNotNull(found);
            System.out.println("Get User: " + found);
        }

        // Test findAll()
        List<User> users1 =  repository.findAll();
        assertEquals(3, users1.size());
        for(User user : users1) {
            System.out.println("findAll User: " + user);
            repository.delete(user.getId());
        }
    }
}
