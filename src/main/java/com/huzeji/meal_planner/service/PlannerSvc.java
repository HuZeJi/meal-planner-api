package com.huzeji.meal_planner.service;

import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import com.huzeji.meal_planner.model.dto.DailyPlanDto;
import com.huzeji.meal_planner.model.dto.WeeklyPlanDto;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListItemDto;
import com.huzeji.meal_planner.model.dto.gpt.recommendation.ShoppingListRecommendationDto;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface PlannerSvc {
    WeeklyPlanEntity generateWeeklyPlan( WeeklyPlanDto weeklyPlanDto );
    WeeklyPlanDto getWeeklyPlan( LocalDate startDate, UUID userId );
    DailyPlanDto getDailyPlan( LocalDate date, UUID userId );
    Mono<ShoppingListRecommendationDto> getShoppingList(UUID weeklyPlanId );
}
