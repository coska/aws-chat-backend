package com.coska.aws.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.coska.aws.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@Repository
public class DynamoRepository<T> {

    private final DynamoDBMapper mapper;

    public DynamoRepository(@Autowired final DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public T save(T item) {
        mapper.save(item);
        return item;
    }

    public List<T> scan(final Class clazz) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        return mapper.scan(clazz, scanExpression);
    }
    public List<T> scan(final Class clazz, String pk) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Map<String, AttributeValue> valuesMap = new HashMap<>();
        valuesMap.put(":val", new AttributeValue().withS(pk));
        scanExpression.withFilterExpression("roomId = :val").withExpressionAttributeValues(valuesMap);
        return mapper.scan(clazz, scanExpression);
    }
    public List<T> findAll(final Class clazz) {
        Map<String, AttributeValue> map = new HashMap<>();

        return mapper.query(clazz,
                new DynamoDBQueryExpression<T>()
                        .withConsistentRead(false));
    }

    public T find(final Class clazz, String pk) {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put(":partitionKey", new AttributeValue(pk));

        List<T> items = mapper.query(clazz,
                new DynamoDBQueryExpression<T>()
                        .withConsistentRead(false)
                        .withKeyConditionExpression("PK = :partitionKey")
                        .withExpressionAttributeValues(map));

        if (items.isEmpty()) {
            return null;
        }
        else if (items.size() == 1) {
            return items.get(0);
        }
        else {
            throw new RuntimeException("Non unique");
        }
    }

    public T get(final Class clazz, String pk) {
        return (T)mapper.load(clazz, pk);
    }

    public T get(final Class clazz, String pk, String sk) {
        return (T)mapper.load(clazz, pk, sk);
    }
    public void delete(final Class clazz, final String pk) {
        T found = get(clazz, pk);
        if (found != null)
            mapper.delete(found);
    }

    public void delete(final Class clazz) {
        mapper.delete(clazz);
    }
    public List<T> query(final Class clazz , DynamoDBQueryExpression<T> queryExpression) {
           return mapper.query(clazz,queryExpression);
    }
}