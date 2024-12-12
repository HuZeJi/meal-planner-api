package com.huzeji.meal_planner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.huzeji.meal_planner.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table( name = "daily_plan", schema = "meal_planner")
@Data
public class DailyPlanEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private UUID id;
    private LocalDate date;

    private StatusEnum status;

    @ManyToOne
    @JoinColumn( name = "weekly_plan_id", nullable = false )
    @JsonBackReference( value = "weekly_plan-daily_plan" )
    private WeeklyPlanEntity weeklyPlan;

    @OneToMany( mappedBy = "dailyPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JsonManagedReference( value = "daily_plan-meal" )
    private List<MealEntity> meals;
}
