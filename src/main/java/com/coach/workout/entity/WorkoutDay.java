package com.coach.workout.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "workout_days")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WorkoutDay {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID planId;
    @Column(nullable = false)
    private String dayName;
}
