package com.coska.aws.mapper;

import com.coska.aws.dto.MessageDto;
import com.coska.aws.entity.BotMessage;
import com.coska.aws.entity.Message;
import com.coska.aws.service.ChatBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;



@Mapper(componentModel = "spring")
public interface BotMessageMapper {

    BotMessage fromMessage(Message msg);
    Message toMessage(BotMessage bm);

    String toJson(BotMessage msg) throws JsonProcessingException;

}

//public class BotMessageMapper {
//
//    // (tk) TODO make them configurable
//    private final String MODEL_NAME = "gpt-3.5-turbo";
//    private final float MODEL_TEMPERATURE = 0.7F;
//    private final String CHAT_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
//    private final String TEST_KEY = "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";
//
//
//    private final static ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public BotMessage fromMessage(Message msg) {
//        return new BotMessage(MODEL_NAME, msg.getPayload(), MODEL_TEMPERATURE, msg.getRoomId());
//    }
//
//    @Override
//    public Message toMessage(BotMessage bm) {
//        Message msg = new Message();
//        msg.setId(UUID.randomUUID().toString());
//        msg.setPayload(bm.prompt);
//        msg.setRoomId(bm.roomId);
//        msg.setType("bot");
//        msg.setTimestamp(ZonedDateTime.now(ZoneId.of("America/Toronto")));
//        return msg;
//    }
//
//    public String toJson(BotMessage msg) throws JsonProcessingException {
//        return mapper.writeValueAsString(msg);
//    }