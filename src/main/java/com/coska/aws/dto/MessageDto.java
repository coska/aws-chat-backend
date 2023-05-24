package com.coska.aws.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {

    private String id;
    private String roomId;
    private String senderId;
    private String senderName;
    private String payload;
    private String type;
    private ZonedDateTime timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String validate() {
        final List<String> list = new ArrayList<>();
        if (!StringUtils.hasLength(id))
            list.add("'id' is empty");

        if (ObjectUtils.isEmpty(list))
            return null;
        else
            return String.join(", ", list);
    }

    
}
