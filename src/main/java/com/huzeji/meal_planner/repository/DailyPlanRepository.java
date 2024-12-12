package com.huzeji.meal_planner.repository;


import com.huzeji.meal_planner.model.DailyPlanEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface DailyPlanRepository
        extends GenericRepository<DailyPlanEntity, UUID>
                , DailyPlanCustomRepository
{
}
