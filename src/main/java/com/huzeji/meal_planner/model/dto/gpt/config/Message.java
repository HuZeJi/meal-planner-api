package com.huzeji.meal_planner.model.dto.gpt.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String role;
    private List<Content> content;
    public Message(String role, List<Content> content) {
        this.role = role;
        this.content = content;
    }
}