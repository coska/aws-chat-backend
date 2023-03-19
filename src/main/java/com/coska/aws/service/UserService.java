package com.coska.aws.service;

import java.util.*;
import java.util.stream.Collectors;

import com.coska.aws.dto.*;
import com.coska.aws.exception.NotFoundException;
import com.coska.aws.exception.ValidationException;
import com.coska.aws.mapper.UserMapper;
import com.coska.aws.entity.User;
import com.coska.aws.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository repository;

    private final UserMapper mapper;

    public UserService(final UserRepository er, final UserMapper mapper) {
        this.repository = er;
        this.mapper = mapper;
    }

    public User save(final User user) {
        return repository.save(user);
    }

    public UserDto create(final UserDto dto) {
        final User user = mapper.toEntity(dto);
        return mapper.toDto(create(user));
    }

    public User create(final User user) {
        if (!StringUtils.hasLength(user.getId()))
        {
            throw new ValidationException(ValidationException.Type.Empty, "id");
        }

        final User found = repository.find(user);
        if (found != null) {
            throw new ValidationException(ValidationException.Type.Duplicated, user.getId());
        }

        return save(user);
    }

    public List<UserDto> findAll() {
        final List<User> users = repository.findAll();
        return users.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public UserDto findById(final String id) {
        return mapper.toDto(repository.get(id));
    }

    public UserDto update(final UserDto dto) {
        final User found = repository.get(dto.getId());
        if (found == null) {
            return create(dto);
        }

        if (StringUtils.hasLength(dto.getFirstName()))
            found.setFirstName(dto.getFirstName());
        if (StringUtils.hasLength(dto.getLastName()))
            found.setLastName(dto.getLastName());
        if (StringUtils.hasLength(dto.getGptKey()))
            found.setGptKey(dto.getGptKey());
        if (StringUtils.hasLength(dto.getType()))
            found.setType(dto.getType());

        return mapper.toDto(repository.save(found));
    }

    public void delete(String userId) {
        final User user = repository.get(userId);
        if (user == null) {
            throw new NotFoundException("User");
        }
        repository.delete(user);
    }
}