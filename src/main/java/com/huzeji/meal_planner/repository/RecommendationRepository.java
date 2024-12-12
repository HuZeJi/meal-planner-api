package com.huzeji.meal_planner.repository;

import com.huzeji.meal_planner.model.RecommendationEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends GenericRepository<RecommendationEntity, UUID> {
}
