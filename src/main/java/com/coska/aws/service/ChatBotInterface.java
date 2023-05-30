package com.coska.aws.service;

import com.coska.aws.entity.BotMessage;
import com.coska.aws.entity.Message;

import java.io.IOException;

public interface ChatBotInterface {

    Message askQuestion(BotMessage msg) throws Exception;
}
