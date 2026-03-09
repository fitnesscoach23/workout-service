package com.coach.workout.dto;

import java.time.Instant;
import java.util.UUID;

public record ExerciseLibraryResponse(
        UUID id,
        Integer srNo,
        String muscleGroup,
        String exerciseName,
        String musclesTrained,
        String videoUrl,
        Instant createdAt,
        Instant updatedAt
) {}
