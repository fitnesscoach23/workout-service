package com.coach.workout.repository;

import com.coach.workout.entity.WorkoutAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutAssignmentRepository extends JpaRepository<WorkoutAssignment, UUID> {

    List<WorkoutAssignment> findByMemberIdAndCoachEmailAndActiveTrue(UUID memberId, String coachEmail);

    Optional<WorkoutAssignment> findByPlanIdAndMemberIdAndCoachEmail(UUID planId, UUID memberId, String coachEmail);
}
