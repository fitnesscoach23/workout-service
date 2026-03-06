package com.coach.workout.repository;

import com.coach.workout.entity.MemberExerciseAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MemberExerciseAssignmentRepository extends JpaRepository<MemberExerciseAssignment, UUID> {
    List<MemberExerciseAssignment> findByCoachEmailAndMemberIdOrderByAssignedAtDesc(String coachEmail, UUID memberId);

    boolean existsByCoachEmailAndMemberIdAndLibraryExerciseId(String coachEmail, UUID memberId, UUID libraryExerciseId);

    @Transactional
    void deleteByCoachEmailAndMemberIdAndLibraryExerciseId(String coachEmail, UUID memberId, UUID libraryExerciseId);

    @Transactional
    void deleteByLibraryExerciseId(UUID libraryExerciseId);
}
