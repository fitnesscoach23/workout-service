package com.coach.workout.service;

import com.coach.workout.dto.ExerciseLibraryRequest;
import com.coach.workout.dto.ExerciseLibraryResponse;
import com.coach.workout.dto.MemberExerciseAssignmentResponse;
import com.coach.workout.entity.ExerciseLibraryItem;
import com.coach.workout.entity.MemberExerciseAssignment;
import com.coach.workout.repository.ExerciseLibraryRepository;
import com.coach.workout.repository.MemberExerciseAssignmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseLibraryService {

    private final ExerciseLibraryRepository libraryRepo;
    private final MemberExerciseAssignmentRepository assignmentRepo;

    public UUID createExercise(String coachEmail, ExerciseLibraryRequest req) {
        ExerciseLibraryItem item = ExerciseLibraryItem.builder()
                .coachEmail(coachEmail)
                .srNo(req.srNo())
                .muscleGroup(req.muscleGroup())
                .exerciseName(req.exerciseName())
                .videoUrl(req.videoUrl())
                .createdAt(Instant.now())
                .build();
        libraryRepo.save(item);
        return item.getId();
    }

    public List<ExerciseLibraryResponse> listExercises(String coachEmail) {
        return libraryRepo.findByCoachEmailOrderByCreatedAtDesc(coachEmail)
                .stream()
                .map(this::toExerciseResponse)
                .toList();
    }

    public ExerciseLibraryResponse getExercise(String coachEmail, UUID exerciseId) {
        return toExerciseResponse(assertOwnedExercise(coachEmail, exerciseId));
    }

    public void updateExercise(String coachEmail, UUID exerciseId, ExerciseLibraryRequest req) {
        ExerciseLibraryItem item = assertOwnedExercise(coachEmail, exerciseId);
        item.setSrNo(req.srNo());
        item.setMuscleGroup(req.muscleGroup());
        item.setExerciseName(req.exerciseName());
        item.setVideoUrl(req.videoUrl());
        item.setUpdatedAt(Instant.now());
        libraryRepo.save(item);
    }

    @Transactional
    public void deleteExercise(String coachEmail, UUID exerciseId) {
        ExerciseLibraryItem item = assertOwnedExercise(coachEmail, exerciseId);
        assignmentRepo.deleteByLibraryExerciseId(exerciseId);
        libraryRepo.delete(item);
    }

    public UUID assignExerciseToMember(String coachEmail, UUID exerciseId, UUID memberId) {
        assertOwnedExercise(coachEmail, exerciseId);

        if (assignmentRepo.existsByCoachEmailAndMemberIdAndLibraryExerciseId(coachEmail, memberId, exerciseId)) {
            throw new RuntimeException("Exercise is already assigned to this member");
        }

        MemberExerciseAssignment assignment = MemberExerciseAssignment.builder()
                .libraryExerciseId(exerciseId)
                .memberId(memberId)
                .coachEmail(coachEmail)
                .assignedAt(Instant.now())
                .build();

        assignmentRepo.save(assignment);
        return assignment.getId();
    }

    public List<MemberExerciseAssignmentResponse> listAssignedExercises(String coachEmail, UUID memberId) {
        List<MemberExerciseAssignment> assignments =
                assignmentRepo.findByCoachEmailAndMemberIdOrderByAssignedAtDesc(coachEmail, memberId);

        return assignments.stream().map(assignment -> {
            ExerciseLibraryItem item = assertOwnedExercise(coachEmail, assignment.getLibraryExerciseId());
            return new MemberExerciseAssignmentResponse(
                    assignment.getId(),
                    assignment.getMemberId(),
                    item.getId(),
                    item.getSrNo(),
                    item.getMuscleGroup(),
                    item.getExerciseName(),
                    item.getVideoUrl(),
                    assignment.getAssignedAt()
            );
        }).toList();
    }

    @Transactional
    public void unassignExerciseFromMember(String coachEmail, UUID exerciseId, UUID memberId) {
        assertOwnedExercise(coachEmail, exerciseId);
        assignmentRepo.deleteByCoachEmailAndMemberIdAndLibraryExerciseId(coachEmail, memberId, exerciseId);
    }

    private ExerciseLibraryItem assertOwnedExercise(String coachEmail, UUID exerciseId) {
        return libraryRepo.findById(exerciseId)
                .filter(item -> item.getCoachEmail().equals(coachEmail))
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }

    private ExerciseLibraryResponse toExerciseResponse(ExerciseLibraryItem item) {
        return new ExerciseLibraryResponse(
                item.getId(),
                item.getSrNo(),
                item.getMuscleGroup(),
                item.getExerciseName(),
                item.getVideoUrl(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
