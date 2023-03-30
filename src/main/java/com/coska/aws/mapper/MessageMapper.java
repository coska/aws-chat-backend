package com.coska.aws.mapper;

import com.coska.aws.dto.MessageDto;
import com.coska.aws.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageDto dto);

    MessageDto toDto(Message entity);
}
