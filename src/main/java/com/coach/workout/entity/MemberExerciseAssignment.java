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
@Table(name = "member_exercise_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberExerciseAssignment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID libraryExerciseId;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private String coachEmail;

    @Column(nullable = false)
    private Instant assignedAt;
}
