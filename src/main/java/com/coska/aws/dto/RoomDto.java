package com.coska.aws.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDto {
    private String id;
    private String title;
    private String lastMessage;
    private String lastSentUserId;
    private List<String> participants;
    
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
