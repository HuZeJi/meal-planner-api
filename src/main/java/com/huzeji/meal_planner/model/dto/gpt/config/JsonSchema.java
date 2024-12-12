package com.huzeji.meal_planner.model.dto.gpt.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSchema {
    private String name;
    private Schema schema;
    private Boolean strict = Boolean.TRUE;
}
