package com.coach.workout.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutPlanResponse(
        UUID id,
        UUID memberId,
        String title,
        String notes,
        Instant createdAt,
        List<Day> days
) {
    public record Day(
            UUID id,
            String dayName,
            List<Exercise> exercises
    ) {}

    public record Exercise(
            UUID id,
            String name,
            String notes,
            String musclesTrained,
            String videoUrl,
            List<Set> sets
    ) {}

    public record Set(
            UUID id,
            Integer setNumber,
            String reps,
            Double weight,
            Double rpe,
            String tempo,
            String rest
    ) {}
}
