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
@Table(name = "exercise_library")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLibraryItem {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String coachEmail;

    @Column(nullable = false)
    private Integer srNo;

    @Column(nullable = false)
    private String muscleGroup;

    @Column(name = "exercise_name", nullable = false)
    private String exerciseName;

    @Column(name = "muscles_trained")
    private String musclesTrained;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;
}
