package com.coach.workout.dto;

import jakarta.validation.constraints.NotBlank;

public record AddWorkoutDayRequest(
        @NotBlank String dayName
) {}
