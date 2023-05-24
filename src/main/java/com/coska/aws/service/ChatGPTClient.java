package com.coska.aws.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static org.apache.http.impl.client.HttpClients.*;

public class ChatGPTClient {

    private final String endPoint;
    private final String apiKey;

    public ChatGPTClient(String apiEndpoint, String apiKey) {
        this.endPoint = apiEndpoint;
        this.apiKey = apiKey;
    }

    public String getAnswer(String question, String model) throws Exception {
        HttpClient httpClient = createDefault();
        HttpPost httpPost = new HttpPost(endPoint);
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        // Create JSON request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("prompt", question);

        StringEntity requestEntity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);

        // Send HTTP request and get response
        HttpEntity responseEntity = httpClient.execute(httpPost).getEntity();
        String responseJson = EntityUtils.toString(responseEntity);
        JSONObject response = new JSONObject(responseJson);

        // Extract answer from response
        String answer = response.getJSONArray("choices").getJSONObject(0).getString("text");
        return answer;
    }
}
