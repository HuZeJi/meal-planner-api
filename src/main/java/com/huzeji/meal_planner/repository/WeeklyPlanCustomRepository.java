package com.huzeji.meal_planner.repository;


import com.huzeji.meal_planner.model.UserEntity;
import com.huzeji.meal_planner.model.WeeklyPlanEntity;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyPlanCustomRepository {
    public List<WeeklyPlanEntity> findBetweenDates(LocalDate date, UserEntity user );
}
