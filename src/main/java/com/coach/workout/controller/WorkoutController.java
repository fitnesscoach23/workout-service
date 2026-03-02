package com.coach.workout.controller;

import com.coach.workout.dto.*;
import com.coach.workout.service.WorkoutAssignmentService;
import com.coach.workout.service.WorkoutService;
import com.coach.workout.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService service;
    private final CurrentUserUtil current;
    private final WorkoutAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody CreateWorkoutPlanRequest req) {
        UUID id = service.createPlan(current.coachEmail(), req);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{planId}/days")
    public ResponseEntity<?> addDay(@PathVariable UUID planId, @RequestBody AddWorkoutDayRequest req) {
        UUID id = service.addDay(current.coachEmail(), planId, req);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/days/{dayId}/exercises")
    public ResponseEntity<?> addExercise(@PathVariable UUID dayId, @RequestBody AddExerciseRequest req) {
        UUID id = service.addExercise(current.coachEmail(), dayId, req);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/exercises/{exerciseId}/sets")
    public ResponseEntity<?> addSet(@PathVariable UUID exerciseId, @RequestBody AddExerciseSetRequest req) {
        UUID id = service.addSet(current.coachEmail(), exerciseId, req);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlan(@PathVariable UUID planId) {
        return ResponseEntity.ok(service.getPlan(current.coachEmail(), planId));
    }

    @PostMapping("/{planId}/assign/{memberId}")
    public ResponseEntity<?> assignPlan(@PathVariable UUID planId,
                                        @PathVariable UUID memberId) {
        assignmentService.assignPlan(current.coachEmail(), planId, memberId);
        return ResponseEntity.ok("Workout assigned successfully");
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getActiveWorkout(@PathVariable UUID memberId) {
        UUID planId = assignmentService.getActivePlanId(current.coachEmail(), memberId);
        return ResponseEntity.ok(service.getPlan(current.coachEmail(), planId));
    }

    @GetMapping
    public ResponseEntity<?> listAllPlans() {
        return ResponseEntity.ok(service.listPlans(current.coachEmail()));
    }

    @PutMapping("/exercises/{exerciseId}/full")
    public ResponseEntity<?> saveExercise(
            @PathVariable UUID exerciseId,
            @RequestBody SaveExerciseRequest req
    ) {
        service.saveExercise(current.coachEmail(), exerciseId, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/exercises/{exerciseId}")
    public ResponseEntity<?> deleteExercise(@PathVariable UUID exerciseId) {
        service.deleteExercise(current.coachEmail(), exerciseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/days/{dayId}")
    public ResponseEntity<?> deleteDay(@PathVariable UUID dayId) {
        service.deleteDay(current.coachEmail(), dayId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable UUID planId) {
        service.deletePlan(current.coachEmail(), planId);
        return ResponseEntity.ok().build();
    }



}
