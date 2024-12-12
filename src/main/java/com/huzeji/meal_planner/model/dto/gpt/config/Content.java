package com.huzeji.meal_planner.model.dto.gpt.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {
    private String type;
    private String text;
    public Content(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
