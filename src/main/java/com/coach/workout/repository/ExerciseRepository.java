package com.coach.workout.repository;

import com.coach.workout.entity.Exercise;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    List<Exercise> findByDayId(UUID dayId);
    @Modifying
    @Transactional
    void deleteByDayId(UUID id);
}
