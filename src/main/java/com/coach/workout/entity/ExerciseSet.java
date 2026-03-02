package com.coach.workout.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "exercise_sets")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ExerciseSet {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID exerciseId;
    @Column(nullable = false)
    private Integer setNumber;
    @Column(nullable = false)
    private String reps;
    private BigDecimal weight;
    private BigDecimal rpe;
    private String tempo;
    private String rest;
}
