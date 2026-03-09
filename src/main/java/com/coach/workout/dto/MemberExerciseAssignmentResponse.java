package com.coach.workout.dto;

import java.time.Instant;
import java.util.UUID;

public record MemberExerciseAssignmentResponse(
        UUID assignmentId,
        UUID memberId,
        UUID exerciseId,
        Integer srNo,
        String muscleGroup,
        String exerciseName,
        String musclesTrained,
        String videoUrl,
        Instant assignedAt
) {}
