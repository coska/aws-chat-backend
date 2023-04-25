package com.coska.aws.mapper;

import org.mapstruct.Mapper;

import com.coska.aws.dto.RoomDto;
import com.coska.aws.entity.Room;

@Mapper(componentModel="spring" )
public interface RoomMapper {
    Room toEntity(RoomDto dto);

    RoomDto toDto(Room entity);
}
