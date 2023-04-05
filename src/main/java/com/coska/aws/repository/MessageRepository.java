package com.coska.aws.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.coska.aws.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MessageRepository {

    private final DynamoRepository<Message> dynamoRepository;

    public MessageRepository(@Autowired final DynamoRepository<Message> repository) {
        this.dynamoRepository = repository;
    }

    public Message save(final Message message){
        return dynamoRepository.save(message);
    }

    public List<Message> findAll() {
        return dynamoRepository.scan(Message.class);
    }
    public List<Message> findByRoomId(String roomId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> valuesMap = new HashMap<>();
        valuesMap.put(":val", new AttributeValue().withS(roomId));
        scanExpression.withFilterExpression("roomId = :val").withExpressionAttributeValues(valuesMap);
        return dynamoRepository.scan(Message.class, scanExpression);
    }
    public Message get(final String pk) { return  dynamoRepository.get(Message.class, pk); }
    public Message get(final String messageId, String roomId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.withFilterExpression("roomId = :roomId and id = :id")
                .withExpressionAttributeValues(
                        Map.of(":roomId", new AttributeValue().withS(roomId),
                                ":id", new AttributeValue().withS(messageId)));

        return  dynamoRepository.get(Message.class, scanExpression);
    }

    public Message find(final Message message) { return get(message.getId()); }

    public void delete(final String pk) { dynamoRepository.delete(Message.class, pk); }

    public void delete(final Message message) { dynamoRepository.delete(Message.class, message.getId()); }
}
