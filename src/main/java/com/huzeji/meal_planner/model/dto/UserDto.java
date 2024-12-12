package com.huzeji.meal_planner.model.dto;

import com.huzeji.meal_planner.model.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String nickname;
    private String password;

    public UserDto( UserEntity user ) {
        this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
    }
}
