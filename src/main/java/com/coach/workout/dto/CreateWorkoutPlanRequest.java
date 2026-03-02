package com.coach.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateWorkoutPlanRequest(
        @NotNull UUID memberId,
        @NotBlank String title,
        String notes
) {}
