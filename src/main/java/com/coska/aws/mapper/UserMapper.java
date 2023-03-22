package com.coska.aws.mapper;

import com.coska.aws.dto.UserDto;
import com.coska.aws.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto dto);

    UserDto toDto(User entity);
}