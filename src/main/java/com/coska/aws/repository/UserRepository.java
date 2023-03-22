package com.coska.aws.repository;

import com.coska.aws.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public User find(final User user) {
        return get(user.getId());
    }

    public void delete(final String pk) {
        dynamoRepository.delete(User.class, pk);
    }

    public void delete(final User user) {
        dynamoRepository.delete(User.class, user.getId());
    }
}