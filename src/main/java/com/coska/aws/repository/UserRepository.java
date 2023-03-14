package com.coska.aws.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.coska.aws.exception.NotFoundException;
import com.coska.aws.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class UserRepository {
    private final DynamoRepository<User> dynamoRepository;

    public UserRepository(@Autowired final DynamoRepository<User> repository) {
        this.dynamoRepository = repository;
    }

    public User save(final User user) {
        return dynamoRepository.save(user);
    }

    public List<User> save(final List<User> users) {
        final List<User> result = new ArrayList<>();
        for(User u: users) {
            result.add(dynamoRepository.save(u));
        }
        return result;
    }

    public List<User> findAll() {
        return dynamoRepository.scan(User.class);
    }

    public User get(final String pk) {
        return dynamoRepository.get(User.class, pk);
    }

    public void delete(final String pk) {
        dynamoRepository.delete(User.class, pk);
    }
}