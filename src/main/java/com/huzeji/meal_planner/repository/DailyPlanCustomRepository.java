package com.huzeji.meal_planner.repository;



import com.huzeji.meal_planner.model.DailyPlanEntity;
import com.huzeji.meal_planner.model.UserEntity;

import java.time.LocalDate;
import java.util.List;

public interface DailyPlanCustomRepository {
    List<DailyPlanEntity> findOnDate( LocalDate date, UserEntity user );
}
