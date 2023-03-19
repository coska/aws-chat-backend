package com.coska.aws.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String gptKey;
    private String type;

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
