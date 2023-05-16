package com.coska.aws.service;

import com.coska.aws.entity.BotMessage;
import com.coska.aws.entity.Message;
import com.coska.aws.mapper.BotMessageMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class ChatGPTService implements ChatBotInterface {

    // (tk) TODO make them configurable
    // public static final String MODEL_NAME = "gpt-3.5-turbo";
    public static final String MODEL_NAME = "davinci-codex";

    public static final float MODEL_TEMPERATURE = 0.7F;
    public static final String CHAT_API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
    // public static final String CHAT_API_URL = "https://api.openai.com/v1/engines/davinci/completions";


    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;


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
    public Message askQuestion(BotMessage botMessage) throws Exception {

        //        OkHttpClient client = new OkHttpClient();
        //        String json = mapper.toJson(botMessage);
        //
        //        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        //        Request request = new Request.Builder()
        //                .url(CHAT_API_URL)
        //                .header("Authorization", gptKey)
        //                .post(requestBody)
        //                .build();
        //
        //        try (Response response = client.newCall(request).execute()) {
        //            String responseBody = response.body().string();
        //            return mapper.fromJson(responseBody);
        //            //BotMessage bm = mapper.fromMessage(msg);
        //            //Message msgResult = toMessage(bm);
        //            // return toMessage(bm);
        //        }
        //        return null;

        // (tk) TODO get room creator -> get creator's gptKey instead lastUserId

        String roomId = botMessage.getRoomId();
        var room = roomService.findById(roomId);
        var userId = room.getLastSentUserId();
        var user = userService.findById(userId);
        var gptKey = user.getGptKey();

        ChatGPTClient client = new ChatGPTClient(CHAT_API_URL, gptKey);
        var question = botMessage.getPrompt();
        System.out.println("Q: " + question);
        var answer = client.getAnswer(botMessage.getPrompt(), botMessage.getModel());
        System.out.println("A: " + answer);

        var msg = new Message();
        msg.setRoomId(roomId);
        msg.setId(UUID.randomUUID().toString());
        msg.setType("bot");
        msg.setSenderId("@@GPT");
        msg.setTimestamp(ZonedDateTime.now(ZoneId.of("America/Toronto")));
        msg.setSenderName("@@GPT");

        return msg;
    }
}
