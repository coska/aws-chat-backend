package com.coska.aws.service;

import com.coska.aws.entity.BotMessage;
import com.coska.aws.entity.Message;
import com.coska.aws.mapper.BotMessageMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;

@Service
public class ChatGPTService implements ChatBotService {

    // (tk) TODO make them configurable
    public static final String MODEL_NAME = "gpt-3.5-turbo";
    public static final float MODEL_TEMPERATURE = 0.7F;
    public static final String CHAT_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
    public static final String TEST_KEY = "";

    private static final Logger logger = LogManager.getLogger(ChatGPTService.class);
    private final BotMessageMapper mapper = new BotMessageMapper() {
        @Override
        public BotMessage fromMessage(Message msg) {
            return new BotMessage(MODEL_NAME, msg.getPayload(), MODEL_TEMPERATURE, msg.getRoomId());
        }

        @Override
        public Message toMessage(BotMessage bm) {
            Message msg = new Message();
            msg.setType("gpt");
            msg.setTimestamp(ZonedDateTime.now());
            msg.setRoomId(bm.getRoomId());
            msg.setPayload(bm.getPrompt());
            return msg;
        }

        @Override
        public String toJson(BotMessage msg) throws JsonProcessingException {
            return null;
        }
    };

    public ChatGPTService() {
    }

    @Override
    public Message askQuestion(BotMessage botMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String json = mapper.toJson(botMessage);

        // (tk) TODO get room creator -> get creator's gptKey
        // String gptKey = getGptKeyOfRoom(msg.getRoomId());
        String gptKey = TEST_KEY;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(CHAT_API_URL)
                .header("Authorization", gptKey)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        try {
            String responseBody = response.body().string();
            return mapper.fromJson(responseBody);
            //BotMessage bm = mapper.fromMessage(msg);
            //Message msgResult = toMessage(bm);
            // return toMessage(bm);
        }
        finally {
            response.close();
        }
        return null;
    }
}
