package com.coach.workout.dto;

import jakarta.validation.constraints.NotBlank;

public record AddExerciseRequest(
        @NotBlank String name,
        String notes,
        String videoUrl
) {}
