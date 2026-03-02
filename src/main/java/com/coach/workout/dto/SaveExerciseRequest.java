// SaveExerciseRequest.java
package com.coach.workout.dto;

import java.util.List;
import java.util.UUID;

public record SaveExerciseRequest(
        String name,
        String notes,
        List<ExerciseSetDto> sets
) {
    public record ExerciseSetDto(
            UUID id,
            Integer setNumber,
            String reps,
            Double weight,
            Double rpe,
            String tempo,
            String rest
    ) {}
}
