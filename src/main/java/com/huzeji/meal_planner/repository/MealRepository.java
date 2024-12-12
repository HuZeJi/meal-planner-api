package com.huzeji.meal_planner.repository;


import com.huzeji.meal_planner.model.MealEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealRepository extends GenericRepository<MealEntity, UUID> {
}
