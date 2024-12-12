package com.huzeji.meal_planner.model.dto.gpt.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schema {
    private String type = "object";
    private List<String> required;
    private Map<String, Schema> properties;
    private Schema items;
    private Boolean additionalProperties;
    private String description;

    public void addProperty( String name, Schema property ) {
        if( this.properties == null ) this.properties = new HashMap<>();
        properties.put( name, property );
    }
}