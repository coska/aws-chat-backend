package com.coska.aws.repository;

import com.coska.aws.config.DynamoDBTestConfiguration;
import com.coska.aws.entity.Room;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private RoomRepository repository;

    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;

    @BeforeEach
    public void initDataModel() {
        logger.debug("Initialize Room DataModel");
        dynamoDBTestConfiguration.dynamoDBRoomSetup();

        // in case previous testing didn't clean up
        List<Room> rooms = repository.findAll();
        for( Room room: rooms ) {
        	repository.delete(room.getId());
        }        
    }

    @AfterEach
    public void verifyEmptyDatabase() {
        logger.debug("Room Item Count: " + dynamoDBTestConfiguration.getRoomItemCount());
    }

    @Test
    void roomCRUD() {
    	logger.debug("roomCRUD");
    	List<Room> rooms = new ArrayList();    	
    	// Test save()
    	int testCount = 3;
    	String roomNamePrefix = "Room_";
    	String messagePrefix = "Message_";
    	String titlePrefix = "title_";
    	for( int rc=0; rc<testCount; rc++ ) {
    		Room room_t = new Room();
    		room_t.setId(roomNamePrefix+rc);
    		room_t.setLastMessage(roomNamePrefix + rc);
    		room_t.setLastSentUserId("userIdB_" + rc);
    		room_t.setTitle(titlePrefix + rc);
    		room_t.setLastSentUserId("lastUserId" + rc);
 
    		Room saved = repository.save(room_t);
    		assertNotNull(saved);
    		rooms.add(saved);    		
    	}
        logger.debug("Room Item Count: " + dynamoDBTestConfiguration.getRoomItemCount());
        assertEquals(  rooms.size(),testCount );

        // Test get()
        for( int rc=0; rc<testCount; rc++ ) {
        	Room found = repository.get(rooms.get(rc).getId());
        	assertNotNull(found);
        	logger.debug(" Get Room: " + found );
        }        
        
        // Test findAll()
        List<Room> foundRooms = repository.findAll();
        
        assertNotNull(foundRooms);
        assertEquals(foundRooms.size(), testCount);

        for(Room room: foundRooms ) {
        	repository.delete(room.getId());
        }
        logger.debug("Room Item Count:After deletion " + dynamoDBTestConfiguration.getRoomItemCount());        
        assertEquals(dynamoDBTestConfiguration.getRoomItemCount(),0l);        
        
    }
    
    @Test
    void roomBulkSave() {
    	logger.debug("roomBulkSave"); 
    	
    	List<Room> rooms = new ArrayList();    	
    	// Test save()
    	int testCount = 3;
    	String roomNamePrefix = "RoomB_";
    	String messagePrefix = "MessageB_";
    	String titlePrefix = "titleB_";
    	for( int rc=0; rc<testCount; rc++ ) {
    		Room room_b = new Room();
    		room_b.setId(roomNamePrefix+rc);
    		room_b.setLastMessage(roomNamePrefix + rc);
    		room_b.setLastSentUserId("userIdB_" + rc);
    		room_b.setTitle(titlePrefix + rc);
    		room_b.setLastSentUserId("lastUserIdB_" + rc);

    		rooms.add(room_b);    		
    	}    	
    	 
		List<Room> savedRooms = repository.save(rooms);
		assertNotNull(savedRooms);
		assertEquals(rooms.size(),savedRooms.size());
    }
    
}
