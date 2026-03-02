package com.coach.workout.repository;

import com.coach.workout.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    List<WorkoutPlan> findByMemberId(UUID memberId);
    List<WorkoutPlan> findByCoachEmail(String coachEmail);

}
