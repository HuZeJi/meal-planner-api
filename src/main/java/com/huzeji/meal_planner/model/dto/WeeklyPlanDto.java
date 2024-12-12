package com.huzeji.meal_planner.model.dto;

import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class WeeklyPlanDto {
    private UUID id;
    // TODO: add user logic
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DailyPlanDto> dailyPlans;

    public WeeklyPlanDto( WeeklyPlanEntity entity ) {
        this.id = entity.getId();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.dailyPlans = entity
                .getDailyPlans()
                .stream()
                .map( DailyPlanDto::new )
                .collect( Collectors.toList() );
    }
}
