package com.coach.workout.dto;

public record AddExerciseSetRequest(
        Integer setNumber,
        String reps,
        Double weight,
        Double rpe,
        String tempo,
        String rest
) {}
