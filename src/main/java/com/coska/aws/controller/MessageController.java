package com.coska.aws.controller;

import com.amazonaws.services.amplify.model.NotFoundException;
import com.coska.aws.dto.MessageDto;
import com.coska.aws.service.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/chat/rooms")
public class MessageController {
    private static final Logger logger = LogManager.getLogger(MessageController.class);
    private final MessageService service;

    public MessageController(final MessageService us) {
        this.service = us;
    }
    @GetMapping("/")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        logger.debug("getAllMessages()");
        List<MessageDto> messages = service.findAll();
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/{id}/messages/{messageId}")
    public ResponseEntity<MessageDto> getMessageById(@Parameter(description = "The ID of the Room", example = "Room01")
                                                     @PathVariable("id") final String roomId,
                                                     @Parameter(description = "The ID of the Message", example = "Message01")
                                                     @PathVariable("messageId") final String messageId) {
        logger.debug("getMessageById(roomId="+roomId+", messageId="+messageId+")");
        MessageDto message = service.findById(messageId, roomId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesByRoomId(@Parameter(description = "The ID of the Room", example = "Room01")
                                                                @PathVariable("id") final String roomId) {
        logger.debug("getMessagesByRoomId(roomId="+roomId+")");
        List<MessageDto> messages = service.findByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageDto> createMessage(@RequestBody final MessageDto dto) {
        // Validate request body
        logger.debug("createMessage(roomId="+dto.getRoomId()+", messageId="+dto.getId()+")");
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}/messages/{messageId}")
    public MessageDto updateMessage(@Parameter(description = "The ID of the Room", example = "Room01")
                                    @PathVariable("id") final String roomId,
                                    @Parameter(description = "The ID of the Message", example = "Message01")
                                    @PathVariable("messageId") final String messageId,
                                    @RequestBody final MessageDto dto) {
        logger.debug("updateMessage(roomId="+roomId+", messageId="+messageId+")");
        dto.setId(messageId);
        dto.setRoomId(roomId);
        final MessageDto messageDto = service.update(dto);
        if (messageDto == null) {
            throw new NotFoundException(messageId);
        }
        return messageDto;
    }

    @DeleteMapping("/{id}/messages/{messageId}")
    public void deleteMessage(@Parameter(description = "The ID of the Room", example = "Room01")
                              @PathVariable("id") final String roomId,
                              @Parameter(description = "The ID of the Message", example = "Message01")
                              @PathVariable("messageId") final String messageId) {
        logger.debug("deleteMessage(roomId="+roomId+", messageId="+messageId+")");
        service.delete(messageId, roomId);
    }
}
