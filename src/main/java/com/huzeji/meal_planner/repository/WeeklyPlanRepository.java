package com.huzeji.meal_planner.repository;

import com.huzeji.meal_planner.model.WeeklyPlanEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeeklyPlanRepository
        extends GenericRepository<WeeklyPlanEntity, UUID>
                , WeeklyPlanCustomRepository
{
}
