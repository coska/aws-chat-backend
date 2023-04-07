package com.coska.aws.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.coska.aws.entity.Room;

@Repository
public class RoomRepository {
	
	private final DynamoRepository<Room> dynamoRepository;
    public RoomRepository(@Autowired final DynamoRepository<Room> dynamoRepository) {
    	this.dynamoRepository = dynamoRepository;
    }	
    
    public Room save(final Room room ) {
    	return dynamoRepository.save(room);
    }
    
    public List<Room> save(final List<Room> rooms) {
        final List<Room> result = new ArrayList<>();
        for( Room room: rooms ) {
        	result.add(dynamoRepository.save(room));
        }
        
        return result;
    }   
    
    public List<Room> findAll() {
    	return dynamoRepository.scan(Room.class);
    }    
    
    public Room find(final Room room) {
        return get(room.getId());
    }
    
    public void delete( final String pk) {
    	dynamoRepository.delete(Room.class, pk);
    }
    
    public void delete(final Room room) {
    	dynamoRepository.delete(Room.class, room.getId());
    }
        
    public Room get(final String pk) {
    	return dynamoRepository.get(Room.class, pk);
    }    
    
}
