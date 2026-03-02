package com.coach.workout.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workout_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutAssignment {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Column(name = "plan_id", nullable = false)
    private UUID planId;

    @Column(name = "member_id", nullable = false)
    private UUID memberId;

    @Column(name = "coach_email", nullable = false)
    private String coachEmail;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
}
