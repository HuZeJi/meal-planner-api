package com.huzeji.meal_planner.model.dto.gpt.recommendation;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingListRecommendationDto {
    private List<ShoppingListItemDto> shoppingList;
    private PriceDto approximateTotal;
}
