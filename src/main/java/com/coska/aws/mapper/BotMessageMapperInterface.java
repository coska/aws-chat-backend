package com.coska.aws.mapper;

import com.coska.aws.entity.BotMessage;
import com.coska.aws.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BotMessageMapperInterface {

    BotMessage fromMessage(Message msg);

    Message toMessage(BotMessage bm);

    String toJson(BotMessage msg) throws JsonProcessingException;

    Message toMessage(String str) throws JsonProcessingException;
}
