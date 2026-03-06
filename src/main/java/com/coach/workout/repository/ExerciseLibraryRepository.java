package com.coach.workout.repository;

import com.coach.workout.entity.ExerciseLibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseLibraryRepository extends JpaRepository<ExerciseLibraryItem, UUID> {
    List<ExerciseLibraryItem> findByCoachEmailOrderByCreatedAtDesc(String coachEmail);
}
