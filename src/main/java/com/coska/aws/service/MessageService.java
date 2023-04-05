package com.coska.aws.service;

import com.coska.aws.dto.MessageDto;
import com.coska.aws.entity.Message;
import com.coska.aws.exception.NotFoundException;
import com.coska.aws.exception.ValidationException;
import com.coska.aws.mapper.MessageMapper;
import com.coska.aws.repository.MessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class MessageService {
    private static final Logger logger = LogManager.getLogger(MessageService.class);
    private final MessageRepository repository;
    private final MessageMapper mapper;

    public MessageService(final MessageRepository er, final MessageMapper mapper) {
        this.repository = er;
        this.mapper = mapper;
    }

    public Message save(final Message message) {
        return repository.save(message);
    }

    public MessageDto create(final MessageDto dto) {
        final Message message = mapper.toEntity(dto);

        // Convert saved Message to DTO and return in response
         return mapper.toDto(create(message));
    }

    public Message create(final Message message) {
        if (!StringUtils.hasLength(message.getId())) {
            throw new ValidationException(ValidationException.Type.Empty, "id");
        }

        final Message found = repository.find(message);
        if (found != null) {
            throw new ValidationException(ValidationException.Type.Duplicated, message.getId());
        }

        return save(message);
    }
    public List<MessageDto> findAll() {
        final List<Message> messages = repository.findAll();
        return messages.stream().map(mapper::toDto).collect(Collectors.toList());
    }
    public List<MessageDto> findByRoomId(String roomId) {
        final List<Message> messages = repository.findByRoomId(roomId);
        return messages.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public MessageDto findById(final String id, String roomId) {
        Message found = repository.get(id, roomId);
        if (found == null) {
            throw new NotFoundException(id);
        }

        return mapper.toDto(found);
    }

    public MessageDto update(final MessageDto dto) {
        final Message found = repository.get(dto.getId(), dto.getRoomId());
        if (found == null) {
            return create(dto);
        }

        if (StringUtils.hasLength(dto.getRoomId())) {
            found.setRoomId(dto.getRoomId());
        }
        if (StringUtils.hasLength(dto.getSenderId())) {
            found.setSenderId(dto.getSenderId());
        }
        if (StringUtils.hasLength(dto.getPayload())) {
            found.setPayload(dto.getPayload());
        }
        if (StringUtils.hasLength(dto.getType())) {
            found.setType(dto.getType());
        }
        if (dto.getTimestamp() != null) {
            found.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        }

        return mapper.toDto(repository.save(found));
    }

    public void delete(String messageId, String roomId) {
        final Message message = repository.get(messageId, roomId);
        if (message == null) {
            throw new NotFoundException(messageId);
        }
        repository.delete(message);
    }
}
