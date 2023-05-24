package com.coska.aws.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BotMessage {
    public String model;
    public String prompt;
    public float temperature;
    public String roomId;
}
