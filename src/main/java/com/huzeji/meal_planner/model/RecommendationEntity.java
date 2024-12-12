package com.huzeji.meal_planner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table( name = "recommendation", schema = "meal_planner" )
@Data
public class RecommendationEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private UUID id;

    @Type(JsonBinaryType.class)  // Directly specify the JSONB type
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode recommendation;

    @ManyToOne
    @JsonBackReference( value = "user-recommendation" )
    private UserEntity user;
}
