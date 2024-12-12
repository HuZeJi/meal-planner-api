package com.huzeji.meal_planner.model.dto;

import com.huzeji.meal_planner.model.DailyPlanEntity;
import com.huzeji.meal_planner.model.MealEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class DailyPlanDto {
    private UUID id;
    private LocalDate date;
    private List<MealDto> meals;

    public DailyPlanDto( DailyPlanEntity entity ) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.meals = entity
                .getMeals()
                .stream()
                .map( MealDto::new )
                .collect( Collectors.toList() );
    }

    public static String generatePrompt( List<DailyPlanEntity> dailyPlans ) {
        return dailyPlans.stream().map( DailyPlanDto::generatePrompt ).collect(Collectors.joining("\n"));
    }

    private static String generatePrompt( DailyPlanEntity dailyPlanEntity ) {
        StringBuilder prompt = new StringBuilder();
        prompt.append( "On " );
        prompt.append( dailyPlanEntity.getDate() );
        prompt.append( ", you have " );
        prompt.append( dailyPlanEntity.getMeals().size() );
        prompt.append( " meals planned. They are " );

        String mealPrompts = dailyPlanEntity
                .getMeals()
                .stream()
                .map( DailyPlanDto::generatePrompt )
                .collect(Collectors.joining(", "));
        prompt.append( mealPrompts );
        return prompt.toString();
    }

    private static String generatePrompt( MealEntity meal ) {
        StringBuilder prompt = new StringBuilder();
        prompt.append( meal.getName() );
        prompt.append( ": " );
        prompt.append( defaultMealDescription( meal.getDescription() ) );
        // TODO: Add the ingredients and recipes to the prompt
        return prompt.toString();
    }

    private static String defaultMealDescription( String description ) {
        description = Optional.ofNullable( description ).orElse( "" );
        return description.isEmpty() ? "No description provided" : description;
    }
}
