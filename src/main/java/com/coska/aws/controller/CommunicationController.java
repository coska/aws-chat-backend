package com.coska.aws.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.coska.aws.dto.MessageBean;
import com.coska.aws.dto.MessageDto;
import com.coska.aws.dto.RoomDto;
import com.coska.aws.dto.UserDto;
import com.coska.aws.entity.Message;
import com.coska.aws.mapper.MessageMapper;
import com.coska.aws.repository.MessageRepository;
import com.coska.aws.service.CommunicationService;
import com.coska.aws.service.MessageService;
import com.coska.aws.service.UserService;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true")
public class CommunicationController {

    private static final Logger logger = LoggerFactory.getLogger(CommunicationController.class);
    
    @Autowired
    private MessageMapper mapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    @MessageMapping("/message")
    // @SendTo("/subscribe/room")
    public void send(@Payload MessageDto message) {
        UUID uuid = UUID.randomUUID();
        message.setId(uuid.toString());
        message.setTimestamp(ZonedDateTime.now(ZoneId.of("America/Toronto")));
        
        logger.debug("createMessage(roomId="+message.getRoomId()+", messageId="+message.getId()+")");
        final String str = message.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }
        
        ResponseEntity.status(HttpStatus.CREATED).body(messageService.create(message));

        simpMessagingTemplate.convertAndSend("/subscribe/room/" + message.getRoomId(), message);
    }

    @GetMapping("/message/test")
    public void test(@RequestParam String name, @RequestParam String message, @RequestParam String roomId) {
        MessageBean msg = MessageBean.builder()
                .roomId(roomId)
                .name(name)
                .message(message)
                .time(Calendar.getInstance())
                .build();
        System.out.println(msg);
        simpMessagingTemplate.convertAndSend("/subscribe/room/" + roomId, msg);
    }

    @PostMapping("/sse/rooms/{roomId}/users/{userId}")
    public String addUser(@PathVariable("roomId") String roomId, @PathVariable("userId") String userId) {
        UserDto user = userService.findById(userId);
        communicationService.addUser(roomId, user);
        return "User " + user.getFirstName() + " " + user.getLastName() + " added !";
    }

    @GetMapping("/sse/rooms/{roomId}/users")
    public Flux<ServerSentEvent<List<UserDto>>> streamUsers(@PathVariable("roomId") String roomId) {
        return communicationService.getUsers(roomId);
    }

    @GetMapping("/v1/chat/sse/rooms")
    public Flux<ServerSentEvent<List<RoomDto>>> streamRoomss() {
        return communicationService.getRooms();
    }

    @GetMapping("/v1/chat/messages/{roomId}")
    public ResponseEntity<List<MessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        final List<Message> messages = messageRepository.findByRoomId(roomId);
        return new ResponseEntity<>(messages.stream().map(mapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }
    
}
