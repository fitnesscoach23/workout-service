package com.coach.workout.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workout_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlan {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private String coachEmail;

    @Column(nullable = false)
    private String title;

    private String notes;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant updatedAt;
}
