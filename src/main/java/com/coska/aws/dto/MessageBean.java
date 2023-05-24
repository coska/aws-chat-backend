package com.coska.aws.dto;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBean {
    private String roomId;
    private String name;
    private String message;
    private Calendar time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Calendar getTime() {
        return this.time;
    }

    @Override
    public String toString() {
        return "MessageBean [roomId=" + roomId + ", name=" + name + ", message=" + message + ", time="
                + DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss") + "]";
    }
}
