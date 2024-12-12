package com.huzeji.meal_planner.service;

import com.huzeji.meal_planner.model.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserSvc {
    List<UserEntity> get();
    List<UserEntity> get( Map<String,Object> filter );
}
