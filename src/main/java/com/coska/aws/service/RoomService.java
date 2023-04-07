package com.coska.aws.service;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.coska.aws.dto.RoomDto;
import com.coska.aws.entity.Room;
import com.coska.aws.mapper.RoomMapper;
import com.coska.aws.mapper.UserMapper;
import com.coska.aws.repository.RoomRepository;
import com.coska.aws.repository.UserRepository;
import com.coska.aws.exception.ValidationException;
import com.coska.aws.exception.NotFoundException;

@Service
public class RoomService {
    private static final Logger logger = LogManager.getLogger(RoomService.class);

    private final RoomRepository repository;
    private final RoomMapper mapper;
	
    public RoomService(final RoomRepository repository, final RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }	
    
    public Room save(final Room room) {
        return repository.save(room);
    }

    public RoomDto create(final RoomDto dto) {
        final Room room = mapper.toEntity(dto);
        return mapper.toDto(create(room));
    }	
    
    public Room create(final Room room) {
        if (!StringUtils.hasLength(room.getId()))
        {
            throw new ValidationException(ValidationException.Type.Empty, "id");
        }

        final Room found = repository.find(room);
        if (found != null) {
            throw new ValidationException(ValidationException.Type.Duplicated, room.getId());
        }

        return save(room);
    }
    
    public RoomDto findById(final String id) {
        Room found = repository.get(id);
        if (found == null) {
            throw new NotFoundException(id);
        }

        return mapper.toDto(found);
    }

    public RoomDto update(final RoomDto dto) {
        final Room found = repository.get(dto.getId());
        if (found == null) {
            return create(dto);
        }

        if (StringUtils.hasLength(dto.getTitle()))
            found.setTitle(dto.getTitle());
        if (StringUtils.hasLength(dto.getLastMessage()))
            found.setLastMessage(dto.getLastMessage());
        if (StringUtils.hasLength(dto.getLastSentUserId()))
            found.setLastSentUserId(dto.getLastSentUserId());
        if(dto.getParticipants() != null && dto.getParticipants().size() > 0 ) {
			found.setParticipants(dto.getParticipants());
		} else {
		    found.setParticipants(new ArrayList());
		}

        return mapper.toDto(repository.save(found));
    }
    
    public void delete(String roomId) {
        final Room room = repository.get(roomId);
        if (room == null) {
            throw new NotFoundException(roomId);
        }
        repository.delete(room);
    }    

}
