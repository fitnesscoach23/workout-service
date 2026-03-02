package com.coach.workout.repository;

import com.coach.workout.entity.WorkoutDay;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.UUID;

public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, UUID> {
    List<WorkoutDay> findByPlanId(UUID planId);
    @Modifying
    @Transactional
    void deleteByPlanId(UUID planId);
}
