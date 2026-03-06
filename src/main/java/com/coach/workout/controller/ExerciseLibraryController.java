package com.coach.workout.controller;

import com.coach.workout.dto.ExerciseLibraryRequest;
import com.coach.workout.service.ExerciseLibraryService;
import com.coach.workout.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/exercise-library")
@RequiredArgsConstructor
public class ExerciseLibraryController {

    private final ExerciseLibraryService service;
    private final CurrentUserUtil current;

    @PostMapping
    public ResponseEntity<?> createExercise(@RequestBody ExerciseLibraryRequest req) {
        UUID id = service.createExercise(current.coachEmail(), req);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<?> listExercises() {
        return ResponseEntity.ok(service.listExercises(current.coachEmail()));
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<?> getExercise(@PathVariable UUID exerciseId) {
        return ResponseEntity.ok(service.getExercise(current.coachEmail(), exerciseId));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<?> updateExercise(
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseLibraryRequest req
    ) {
        service.updateExercise(current.coachEmail(), exerciseId, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<?> deleteExercise(@PathVariable UUID exerciseId) {
        service.deleteExercise(current.coachEmail(), exerciseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{exerciseId}/assign/{memberId}")
    public ResponseEntity<?> assignExercise(
            @PathVariable UUID exerciseId,
            @PathVariable UUID memberId
    ) {
        UUID assignmentId = service.assignExerciseToMember(current.coachEmail(), exerciseId, memberId);
        return ResponseEntity.ok(assignmentId);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> listAssignedExercises(@PathVariable UUID memberId) {
        return ResponseEntity.ok(service.listAssignedExercises(current.coachEmail(), memberId));
    }

    @DeleteMapping("/{exerciseId}/member/{memberId}")
    public ResponseEntity<?> unassignExercise(
            @PathVariable UUID exerciseId,
            @PathVariable UUID memberId
    ) {
        service.unassignExerciseFromMember(current.coachEmail(), exerciseId, memberId);
        return ResponseEntity.ok().build();
    }
}
