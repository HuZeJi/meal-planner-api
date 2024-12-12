package com.huzeji.meal_planner.service.impl;

import com.huzeji.meal_planner.model.UserEntity;
import com.huzeji.meal_planner.repository.UserRepository;
import com.huzeji.meal_planner.service.UserSvc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class UserSvcImpl implements UserSvc {
    private final UserRepository userRepository;
    public UserSvcImpl( UserRepository userRepository ) {
        this.userRepository = userRepository;
    }
    public List<UserEntity> get() {
        return userRepository.getAll( Map.of() );
    }
    public List<UserEntity> get( Map<String, Object> filter ) {
        return userRepository.getAll( filter );
    }
}
