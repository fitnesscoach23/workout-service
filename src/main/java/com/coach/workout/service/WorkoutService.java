package com.coach.workout.service;

import com.coach.workout.dto.*;
import com.coach.workout.entity.*;
import com.coach.workout.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutPlanRepository planRepo;
    private final WorkoutDayRepository dayRepo;
    private final ExerciseRepository exerciseRepo;
    private final ExerciseSetRepository setRepo;

    public UUID createPlan(String coachEmail, CreateWorkoutPlanRequest req) {
        WorkoutPlan plan = WorkoutPlan.builder()
                .memberId(req.memberId())
                .coachEmail(coachEmail)
                .title(req.title())
                .notes(req.notes())
                .createdAt(Instant.now())
                .build();
        planRepo.save(plan);
        return plan.getId();
    }

    public UUID addDay(String coachEmail, UUID planId, AddWorkoutDayRequest req) {
        WorkoutPlan plan = assertOwnedPlan(coachEmail, planId);

        WorkoutDay day = WorkoutDay.builder()
                .planId(planId)
                .dayName(req.dayName())
                .build();

        dayRepo.save(day);
        return day.getId();
    }

    public UUID addExercise(String coachEmail, UUID dayId, AddExerciseRequest req) {
        WorkoutDay day = dayRepo.findById(dayId)
                .orElseThrow(() -> new RuntimeException("Day not found"));

        assertOwnedPlan(coachEmail, day.getPlanId());

        Exercise ex = Exercise.builder()
                .dayId(dayId)
                .name(req.name())
                .notes(req.notes())
                .videoUrl(req.videoUrl())
                .build();

        exerciseRepo.save(ex);
        return ex.getId();
    }

    public UUID addSet(String coachEmail, UUID exerciseId, AddExerciseSetRequest req) {
        Exercise ex = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        WorkoutDay day = dayRepo.findById(ex.getDayId())
                .orElseThrow(() -> new RuntimeException("Day not found"));

        assertOwnedPlan(coachEmail, day.getPlanId());

        ExerciseSet set = ExerciseSet.builder()
                .exerciseId(exerciseId)
                .setNumber(req.setNumber())
                .reps(req.reps())
                .weight(req.weight() != null ? java.math.BigDecimal.valueOf(req.weight()) : null)
                .rpe(req.rpe() != null ? java.math.BigDecimal.valueOf(req.rpe()) : null)
                .tempo(req.tempo())
                .rest(req.rest())
                .build();

        setRepo.save(set);
        return set.getId();
    }

    public WorkoutPlanResponse getPlan(String coachEmail, UUID planId) {
        WorkoutPlan plan = assertOwnedPlan(coachEmail, planId);

        List<WorkoutDay> days = dayRepo.findByPlanId(planId);

        List<WorkoutPlanResponse.Day> dayResponses = days.stream().map(day -> {

            List<Exercise> exercises = exerciseRepo.findByDayId(day.getId());

            List<WorkoutPlanResponse.Exercise> exResponses = exercises.stream().map(ex -> {

                List<ExerciseSet> sets = setRepo.findByExerciseId(ex.getId());

                List<WorkoutPlanResponse.Set> setResponses = sets.stream().map(st ->
                        new WorkoutPlanResponse.Set(
                                st.getId(),
                                st.getSetNumber(),
                                st.getReps(),
                                st.getWeight() != null ? st.getWeight().doubleValue() : null,
                                st.getRpe() != null ? st.getRpe().doubleValue() : null,
                                st.getTempo(),
                                st.getRest()
                        )
                ).toList();

                return new WorkoutPlanResponse.Exercise(
                        ex.getId(),
                        ex.getName(),
                        ex.getNotes(),
                        ex.getVideoUrl(),
                        setResponses
                );

            }).toList();

            return new WorkoutPlanResponse.Day(
                    day.getId(),
                    day.getDayName(),
                    exResponses
            );

        }).toList();

        return new WorkoutPlanResponse(
                plan.getId(),
                plan.getMemberId(),
                plan.getTitle(),
                plan.getNotes(),
                plan.getCreatedAt(),
                dayResponses
        );
    }

    private WorkoutPlan assertOwnedPlan(String coachEmail, UUID planId) {
        return planRepo.findById(planId)
                .filter(plan -> plan.getCoachEmail().equals(coachEmail))
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public List<WorkoutPlanResponse> listPlans(String coachEmail) {

        List<WorkoutPlan> plans = planRepo.findByCoachEmail(coachEmail);

        return plans.stream()
                .map(plan -> getPlan(coachEmail, plan.getId()))
                .toList();
    }

    @Transactional
    public void saveExercise(
            String coachEmail,
            UUID exerciseId,
            SaveExerciseRequest req
    ) {
        Exercise ex = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        WorkoutDay day = dayRepo.findById(ex.getDayId())
                .orElseThrow(() -> new RuntimeException("Day not found"));

        assertOwnedPlan(coachEmail, day.getPlanId());

        // update exercise
        ex.setName(req.name());
        ex.setNotes(req.notes());
        ex.setVideoUrl(req.videoUrl());
        exerciseRepo.save(ex);

        List<ExerciseSet> existingSets = setRepo.findByExerciseId(exerciseId);

        Map<UUID, ExerciseSet> existingMap = new HashMap<>();
        for (ExerciseSet s : existingSets) {
            existingMap.put(s.getId(), s);
        }

        Set<UUID> incomingIds = req.sets().stream()
                .map(SaveExerciseRequest.ExerciseSetDto::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // delete removed
        existingSets.stream()
                .filter(s -> !incomingIds.contains(s.getId()))
                .forEach(setRepo::delete);

        // upsert
        for (SaveExerciseRequest.ExerciseSetDto dto : req.sets()) {
            if (dto.id() != null) {
                ExerciseSet s = existingMap.get(dto.id());
                s.setSetNumber(dto.setNumber());
                s.setReps(dto.reps());
                s.setWeight(dto.weight() != null ? BigDecimal.valueOf(dto.weight()) : null);
                s.setRpe(dto.rpe() != null ? BigDecimal.valueOf(dto.rpe()) : null);
                s.setTempo(dto.tempo());
                s.setRest(dto.rest());
                setRepo.save(s);
            } else {
                ExerciseSet s = ExerciseSet.builder()
                        .exerciseId(exerciseId)
                        .setNumber(dto.setNumber())
                        .reps(dto.reps())
                        .weight(dto.weight() != null ? BigDecimal.valueOf(dto.weight()) : null)
                        .rpe(dto.rpe() != null ? BigDecimal.valueOf(dto.rpe()) : null)
                        .tempo(dto.tempo())
                        .rest(dto.rest())
                        .build();
                setRepo.save(s);
            }
        }
    }

    @Transactional
    public void deleteExercise(String coachEmail, UUID exerciseId) {

        Exercise ex = exerciseRepo.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        WorkoutDay day = dayRepo.findById(ex.getDayId())
                .orElseThrow(() -> new RuntimeException("Day not found"));

        // ownership check
        assertOwnedPlan(coachEmail, day.getPlanId());

        // delete sets first
        setRepo.deleteByExerciseId(exerciseId);

        // delete exercise
        exerciseRepo.delete(ex);
    }

    @Transactional
    public void deleteDay(String coachEmail, UUID dayId) {

        WorkoutDay day = dayRepo.findById(dayId)
                .orElseThrow(() -> new RuntimeException("Day not found"));

        // ownership check
        assertOwnedPlan(coachEmail, day.getPlanId());

        // fetch exercises
        List<Exercise> exercises = exerciseRepo.findByDayId(dayId);

        // delete sets for each exercise
        for (Exercise ex : exercises) {
            setRepo.deleteByExerciseId(ex.getId());
        }

        // delete exercises
        exerciseRepo.deleteAll(exercises);

        // delete day
        dayRepo.delete(day);
    }

    @Transactional
    public void deletePlan(String coachEmail, UUID planId) {

        WorkoutPlan plan = assertOwnedPlan(coachEmail, planId);

        List<WorkoutDay> days = dayRepo.findByPlanId(planId);

        for (WorkoutDay day : days) {

            List<Exercise> exercises = exerciseRepo.findByDayId(day.getId());

            for (Exercise ex : exercises) {
                setRepo.deleteByExerciseId(ex.getId());
            }

            exerciseRepo.deleteByDayId(day.getId());
        }

        dayRepo.deleteByPlanId(planId);
        planRepo.delete(plan);
    }


}
