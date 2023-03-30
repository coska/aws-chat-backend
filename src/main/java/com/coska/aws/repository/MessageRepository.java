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

    public List<Message> save(final List<Message> messages){
        final List<Message> result = new ArrayList<>();
        for (Message m: messages){
            result.add(dynamoRepository.save(m));
        }
        return result;
    }

    public List<Message> findAll() {
        return dynamoRepository.scan(Message.class);
    }
    public List<Message> findByRoomId(String roomId) {
        return dynamoRepository.scan(Message.class, roomId);
    }
    public Message get(final String pk) { return  dynamoRepository.get(Message.class, pk); }
    public Message get(final String pk, String sk) { return  dynamoRepository.get(Message.class, pk, sk); }

    public Message find(final Message message) { return get(message.getId()); }

    public void delete(final String pk) { dynamoRepository.delete(Message.class, pk); }

    public void delete(final Message message) { dynamoRepository.delete(Message.class, message.getId()); }

    public List<Message> getMessagesByIndex(final String indexName, final String attributeName, final String attributeValue) {
        final DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<>();
        final Message message = new Message();
        message.setSenderId(attributeValue);
        queryExpression.withIndexName(indexName)
                .withConsistentRead(false)
                .withKeyConditionExpression(attributeName + " = :val")
                .withExpressionAttributeValues(Collections.singletonMap(":val", new AttributeValue(attributeValue)))
                .withScanIndexForward(false); // To sort results in descending order of timestamp
        final List<Message> messages = dynamoRepository.query(Message.class, queryExpression);
        return messages;
    }
}
