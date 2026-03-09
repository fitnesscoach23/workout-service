package com.coach.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExerciseLibraryRequest(
        @NotNull Integer srNo,
        @NotBlank String muscleGroup,
        @NotBlank String exerciseName,
        String musclesTrained,
        String videoUrl
) {}
