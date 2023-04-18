package com.coska.aws.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coska.aws.dto.MessageBean;

@RestController
public class SokcetController {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/message")
    //@SendTo("/subscribe/room")
    public void send(@Payload MessageBean message) {
        message.setTime(Calendar.getInstance());
        simpMessagingTemplate.convertAndSend("/subscribe/room/" + message.getRoomId(), message);
        System.out.println(message);
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

}
