package com.coska.aws.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.coska.aws.dto.MessageBean;
import com.coska.aws.dto.RoomDto;

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
				rooms.add(room.getTitle());
			}
		}
	}

	List<String> rooms = new ArrayList<String>();
	Map<String, List<String>> roomUsers = new HashMap<String, List<String>>();
	Map<String, List<MessageBean>> rommMessages = new HashMap<String, List<MessageBean>>();

	public void addUser(String roomId, String name) {
		if (roomUsers.containsKey(roomId)) {
			List<String> users = roomUsers.get(roomId);
			if (!users.contains(name)) {
				users.add(name);
				roomUsers.put(roomId, users);
			}
		} else {
			List<String> users = new ArrayList<String>();
			users.add(name);
			roomUsers.put(roomId, users);
		}

		if (!rommMessages.containsKey(roomId)) {
			List<MessageBean> messages = new ArrayList<MessageBean>();
			MessageBean message = MessageBean.builder().roomId(roomId).name(name).message("welcome").build();
			messages.add(message);
			rommMessages.put(roomId, messages);
		}
	}


	public Flux<ServerSentEvent<List<String>>> getUsers(String roomId) {
		if (roomId != null && !roomId.isBlank()) {
			return Flux.interval(Duration.ofSeconds(1))
					.map(sequence -> ServerSentEvent.<List<String>>builder().id(String.valueOf(sequence))
							.event("user-list-event").data(roomUsers.get(roomId)).build());
		}

		return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<String>>builder()
				.id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
	}

	public Flux<ServerSentEvent<List<String>>> getRooms() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(sequence -> ServerSentEvent.<List<String>>builder().id(String.valueOf(sequence))
						.event("room-list-event").data(rooms).build());

	}
	
	public void addRoom(RoomDto dto) {
		rooms.add(dto.getTitle());
	}

}
