package com.huzeji.meal_planner.model.dto.gpt.recommendation;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingListItemDto {
    private String name;
    private int quantity;
    private String quantityUnit;
    private PriceDto approximateSubTotal;
    private List<RequiredTimeDto> requiredAt;
    private String ingredientType;
}
