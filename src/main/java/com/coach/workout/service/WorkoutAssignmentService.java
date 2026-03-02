package com.coach.workout.service;

import com.coach.workout.entity.WorkoutAssignment;
import com.coach.workout.repository.WorkoutAssignmentRepository;
import com.coach.workout.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutAssignmentService {

    private final WorkoutAssignmentRepository assignmentRepo;
    private final WorkoutPlanRepository planRepo;

    public void assignPlan(String coachEmail, UUID planId, UUID memberId) {

        // Validate plan ownership
        planRepo.findById(planId)
                .filter(p -> p.getCoachEmail().equals(coachEmail))
                .orElseThrow(() -> new RuntimeException("Workout plan not found or access denied"));

        // Deactivate existing active assignments
        List<WorkoutAssignment> activeAssignments =
                assignmentRepo.findByMemberIdAndCoachEmailAndActiveTrue(memberId, coachEmail);

        for (WorkoutAssignment a : activeAssignments) {
            a.setActive(false);
            assignmentRepo.save(a);
        }

        // Create new assignment
        WorkoutAssignment assignment = WorkoutAssignment.builder()
                .planId(planId)
                .memberId(memberId)
                .coachEmail(coachEmail)
                .active(true)
                .assignedAt(Instant.now())
                .build();

        assignmentRepo.save(assignment);
    }

    public UUID getActivePlanId(String coachEmail, UUID memberId) {
        return assignmentRepo.findByMemberIdAndCoachEmailAndActiveTrue(memberId, coachEmail)
                .stream()
                .findFirst()
                .map(WorkoutAssignment::getPlanId)
                .orElseThrow(() -> new RuntimeException("No active workout assigned"));
    }
}
