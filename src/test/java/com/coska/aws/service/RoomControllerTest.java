package com.coska.aws.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coska.aws.config.DynamoDBTestConfiguration;
import com.coska.aws.controller.UserController;
import com.coska.aws.dto.RoomDto;
import com.coska.aws.entity.Message;
import com.coska.aws.entity.Room;
import com.coska.aws.mapper.RoomMapper;
import com.coska.aws.repository.RoomRepository;
import com.coska.aws.service.base.BaseControllerTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
public class RoomControllerTest extends BaseControllerTest {
    
	private static final Logger logger = LogManager.getLogger(RoomControllerTest.class);
    
	@Autowired
	private RoomRepository  repository;
	
	@Autowired	
	private RoomService  service;
	
	@Autowired
	private RoomMapper  mapper;	
	
    @Autowired
    private DynamoDBTestConfiguration dynamoDBTestConfiguration;
	
    @BeforeEach
    public void clearRoomData() {
        logger.debug("Initialize Message DataModel");
        dynamoDBTestConfiguration.dynamoDBMessageSetup();

        // in case previous testing didn't clean up
        List<Room> rooms =  repository.findAll();
        for(Room room : rooms) {
            repository.delete(room.getId());
        }
    }	
    
	@Test
	public void createRoom() throws Exception {    
		RoomDto roomDto = RoomDto.builder()
				.id("Room_100")
				.title("SamRoom")
				.lastMessage("LastTikTok")
				.lastSentUserId("lastUser0000")
				.participants(new ArrayList())
				.build();
		mockMvc.perform(post("/v1/chat/rooms")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(roomDto))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").exists())
			;
	}
	
	@Test
	public void createRoom_withBadData() throws Exception {    
		RoomDto roomDto = RoomDto.builder()
				.id(null)
				.title("SamRoom")
				.lastMessage("LastTikTok")
				.lastSentUserId("lastUser0000")
				.participants(new ArrayList())
				.build();
				mockMvc.perform(post("/v1/chat/rooms")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(roomDto))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	//	.andExpect(jsonPath("id").exists())
	}	
	
	@Test //
	public void updateRoom() throws Exception{
		Room room = this.generateRoom(10);

		// Given
		String roomTitle = "Updated Room!!";
		RoomDto roomDto = mapper.toDto(room);
		roomDto.setTitle(roomTitle);
		
		// When
		this.mockMvc.perform(put("/v1/chat/rooms")
		//		.header(HttpHeaders.AUTHORIZATION, getBearerAuthToken())				
 				.content(objectMapper.writeValueAsString(roomDto))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
 				)
		        .andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("title").value(roomTitle))
		;

	}
	
	@Test // retrieve one event 
	public void getRoom() throws Exception {
		int roomNum = 10;
		String roomId = "Room_";
		Room room = this.generateRoom(roomNum);		
		this.mockMvc.perform(get("/v1/chat/rooms/{id}",room.getId())
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").value(roomId + roomNum))
		;
	}

	@Test // retrieve room list 
	public void getRoomList() throws Exception {
		int roomNum = 10;
		String roomId = "Room_";
		int roomSize = 30;
		List<Room> rooms = new ArrayList();
		for( int idx = 0; idx<roomSize ; idx++ ) {
			rooms.add(this.generateRoom(idx));			
		}

		this.mockMvc.perform(get("/v1/chat/rooms")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").exists()) 
		.andExpect(jsonPath("$[0].title").exists())			
		;
	}
	
	@Test // retrieve room list 
	public void deleteExistingRoom() throws Exception {
		int roomNum = 10;
		String roomIdPrefix = "Room_";
		int roomSize = 30;
		List<Room> rooms = new ArrayList();
		for( int idx = 0; idx<roomSize ; idx++ ) {
			rooms.add(this.generateRoom(idx));			
		}

		this.mockMvc.perform(delete("/v1/chat/rooms/{id}",roomIdPrefix + roomNum )
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				)
		.andDo(print())
		.andExpect(status().isOk())
		;
	}	

	@Test // retrieve room list 
	public void deleteNonExistingRoom() throws Exception {
		int roomNum = 10;
		int nonExisting_roomNum = 40;		
		String roomIdPrefix = "Room_";
		int roomSize = 30;
		List<Room> rooms = new ArrayList();
		for( int idx = 0; idx<roomSize ; idx++ ) {
			rooms.add(this.generateRoom(idx));			
		}

		this.mockMvc.perform(delete("/v1/chat/rooms/{id}",roomIdPrefix + nonExisting_roomNum )
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				)
		.andDo(print())
		.andExpect(status().isNotFound())
		;
	}	
		
	private Room generateRoom(int index) {

		RoomDto roomDto = RoomDto.builder()
				.id("Room_" + index)
				.title("ChatRoom_" + index )
				.lastMessage("LastTikTok")
				.lastSentUserId("lastUser_0" + index )
				.participants(new ArrayList())
				.build();		
		
		return  service.save(roomDto) ;
	}	
	
}
