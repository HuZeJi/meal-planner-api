package com.huzeji.meal_planner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.huzeji.meal_planner.model.enums.StatusEnum;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table( name = "weekly_plan", schema = "meal_planner" )
@Data
public class WeeklyPlanEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private UUID id;

    private LocalDate startDate;
    private LocalDate endDate;
    private StatusEnum status = StatusEnum.ACTIVE;

    @ManyToOne
    @JoinColumn( name = "user_id", nullable = false )
    @JsonBackReference( value = "user-weekly_plan" )
    private UserEntity user;

    @OneToMany( mappedBy = "weeklyPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JsonManagedReference( value = "weekly_plan-daily_plan" )
    private List<DailyPlanEntity> dailyPlans = List.of();

    @Type(JsonType.class)
    @Column(name = "shopping_list", columnDefinition = "json")
    private JsonNode shoppingList;
}
