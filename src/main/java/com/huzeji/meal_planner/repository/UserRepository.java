package com.huzeji.meal_planner.repository;

import com.huzeji.meal_planner.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends GenericRepository<UserEntity, UUID> {
}
