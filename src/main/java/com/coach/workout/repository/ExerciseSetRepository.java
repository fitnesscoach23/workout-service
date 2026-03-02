package com.coach.workout.repository;

import com.coach.workout.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, UUID> {
    List<ExerciseSet> findByExerciseId(UUID exerciseId);

    @Modifying
    @Query("delete from ExerciseSet s where s.exerciseId = :exerciseId")
    void deleteByExerciseId(UUID exerciseId);
}
