package com.huzeji.meal_planner.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.huzeji.meal_planner.model.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table( name = "user", schema = "meal_planner" )
@Data
@NoArgsConstructor
public class UserEntity {

    public UserEntity( UserDto userDto ) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.nickname = userDto.getNickname();
        this.password = userDto.getPassword();
    }

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private UUID id;
    private String name;
    private String nickname;
    private String password;

    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JsonManagedReference( value = "user-weekly_plan" )
    private List<WeeklyPlanEntity> weeklyPlan;

    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JsonManagedReference( value = "user-recommendation" )
    private List<RecommendationEntity> recommendations;
}
