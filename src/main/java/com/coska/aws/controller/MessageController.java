package com.coska.aws.controller;

import com.amazonaws.services.amplify.model.BadRequestException;
import com.amazonaws.services.amplify.model.NotFoundException;
import com.coska.aws.dto.MessageDto;
import com.coska.aws.dto.UserDto;
import com.coska.aws.entity.Message;
import com.coska.aws.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping(value = "/v1/chat/rooms")
public class MessageController {
    private static final Logger logger = LogManager.getLogger(MessageController.class);
    private final MessageService service;

    public MessageController(final MessageService us) {
        this.service = us;
    }
    @GetMapping("/{roomId}/messages/{messageId}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable String roomId, @PathVariable String messageId) {
        MessageDto message = service.findById(messageId, roomId);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        List<MessageDto> messages = service.findByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<MessageDto> createMessage(@RequestBody final MessageDto dto) {
        // Validate request body
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }
    @PutMapping("/{id}/messages/{messageId}")
    public MessageDto updateMessage(@PathVariable("id") final String roomId,
                                    @PathVariable("messageId") final String messageId,
                                    @RequestBody final MessageDto dto) {
        dto.setId(messageId);
        dto.setRoomId(roomId);
        final MessageDto messageDto = service.update(dto);
        if (messageDto == null) {
            throw new NotFoundException(messageId);
        }
        return messageDto;
    }
    @DeleteMapping("/{id}/messages/{messageId}")
    public void deleteMessage(@PathVariable("id") final String roomId,
                              @PathVariable("messageId") final String messageId) {
        service.delete(messageId, roomId);
    }
}
