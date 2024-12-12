package com.huzeji.meal_planner.model.dto;

import com.huzeji.meal_planner.model.MealEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MealDto {
    private String name;
    // TODO: add a title
    private String description;
    private String image;
    private String recipe;
    private String ingredients;
    private List<String> tags;
//    times

    public MealDto( MealEntity entity ) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.image = entity.getImage();
        this.recipe = entity.getRecipe();
        this.ingredients = entity.getIngredients();
        // TODO: fix tags management
        this.tags = List.of( entity.getTags().split(",") );
    }
}
