package com.huzeji.meal_planner.model.dto.gpt.recommendation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDto {
    private BigDecimal quantity;
    private String currency;
}
