package com.huzeji.meal_planner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table( name = "meal", schema = "meal_planner" )
@Data
public class MealEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private UUID id;

    private String name;
    private String description;
    private String image;
    private String recipe;
    private String ingredients;
    // TODO: add logic for tags -> separate tags to a new entity
    private String tags;

    @ManyToOne
    @JoinColumn( name = "daily_plan_id", nullable = false )
    @JsonBackReference( value = "daily_plan-meal" )
    private DailyPlanEntity dailyPlan;
}
