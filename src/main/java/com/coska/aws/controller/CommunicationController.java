package com.coska.aws.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coska.aws.dto.MessageBean;
import com.coska.aws.service.CommunicationService;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true")
public class CommunicationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private CommunicationService communicationService;

    @MessageMapping("/message")
    // @SendTo("/subscribe/room")
    public void send(@Payload MessageBean message) {
        message.setTime(Calendar.getInstance());
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

    @PostMapping("/sse/rooms/{roomId}/users/{name}")
    public String addUser(@PathVariable("roomId") String roomId, @PathVariable("name") String name) {
        communicationService.addUser(roomId, name);
        return "User " + name + " added !";
    }

    @GetMapping("/sse/rooms/{roomId}/users")
    public Flux<ServerSentEvent<List<String>>> streamUsers(@PathVariable("roomId") String roomId) {
        return communicationService.getUsers(roomId);
    }

    @GetMapping("/v1/chat/sse/rooms")
    public Flux<ServerSentEvent<List<String>>> streamRoomss() {
        return communicationService.getRooms();
    }
    
}
