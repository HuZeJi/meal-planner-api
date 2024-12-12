package com.huzeji.meal_planner.service;

import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListRecommendationDto;
import reactor.core.publisher.Mono;

public interface OpenAISvc {
    Mono<ShoppingListRecommendationDto> generateShoppingList(WeeklyPlanEntity weeklyPlan );
}
