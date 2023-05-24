package com.coska.aws.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.coska.aws.dto.MessageBean;
import com.coska.aws.dto.RoomDto;
import com.coska.aws.dto.UserDto;

import reactor.core.publisher.Flux;

@Service
public class CommunicationService {
	private static final String NO_MESSAGE_TEXT = "No message yet !";

    private final RoomService service;

	public CommunicationService(RoomService service) {
        this.service = service;
		if (rooms.size() == 0) {
			List<RoomDto> rooms_ = service.findAll();
			for (RoomDto room : rooms_) {
				rooms.add(room);
			}
		}
	}

	// List<String> rooms = new ArrayList<String>();
	List<RoomDto> rooms = new ArrayList<RoomDto>();
	Map<String, List<UserDto>> roomUsers = new HashMap<String, List<UserDto>>();
	Map<String, List<MessageBean>> rommMessages = new HashMap<String, List<MessageBean>>();

	public void addUser(String roomId, UserDto user) {
		if (roomUsers.containsKey(roomId)) {
			List<UserDto> users = roomUsers.get(roomId);
			if (!users.contains(user.getId())) {
				users.add(user);
				roomUsers.put(roomId, users);
			}
		} else {
			List<UserDto> users = new ArrayList<UserDto>();
			users.add(user);
			roomUsers.put(roomId, users);
		}

		// if (!rommMessages.containsKey(roomId)) {
		// 	List<MessageBean> messages = new ArrayList<MessageBean>();
		// 	MessageBean message = MessageBean.builder().roomId(roomId).name(name).message("welcome").build();
		// 	messages.add(message);
		// 	rommMessages.put(roomId, messages);
		// }
	}


	public Flux<ServerSentEvent<List<UserDto>>> getUsers(String roomId) {
		if (roomId != null && !roomId.isBlank()) {
			return Flux.interval(Duration.ofSeconds(1))
					.map(sequence -> ServerSentEvent.<List<UserDto>>builder().id(String.valueOf(sequence))
							.event("user-list-event").data(roomUsers.get(roomId)).build());
		}

		return Flux.interval(Duration.ofSeconds(5)).map(sequence -> ServerSentEvent.<List<UserDto>>builder()
				.id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<UserDto>()).build());
	}

	public Flux<ServerSentEvent<List<RoomDto>>> getRooms() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(sequence -> ServerSentEvent.<List<RoomDto>>builder().id(String.valueOf(sequence))
						.event("room-list-event").data(rooms).build());

	}
	
	public void addRoom(RoomDto dto) {
		rooms.add(dto);
	}

}
