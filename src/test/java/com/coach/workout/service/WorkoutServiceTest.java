package com.coach.workout.service;

import com.coach.workout.dto.UpdateWorkoutPlanRequest;
import com.coach.workout.entity.WorkoutPlan;
import com.coach.workout.repository.ExerciseRepository;
import com.coach.workout.repository.ExerciseSetRepository;
import com.coach.workout.repository.WorkoutDayRepository;
import com.coach.workout.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutPlanRepository planRepo;

    @Mock
    private WorkoutDayRepository dayRepo;

    @Mock
    private ExerciseRepository exerciseRepo;

    @Mock
    private ExerciseSetRepository setRepo;

    @InjectMocks
    private WorkoutService service;

    @Test
    void updatePlanUpdatesTitleAndNotes() {
        UUID planId = UUID.randomUUID();
        WorkoutPlan plan = WorkoutPlan.builder()
                .id(planId)
                .memberId(UUID.randomUUID())
                .coachEmail("coach@example.com")
                .title("Old Title")
                .notes("Old Notes")
                .createdAt(Instant.now())
                .build();

        when(planRepo.findById(planId)).thenReturn(Optional.of(plan));

        service.updatePlan(
                "coach@example.com",
                planId,
                new UpdateWorkoutPlanRequest("Updated Title", "Updated Notes")
        );

        assertEquals("Updated Title", plan.getTitle());
        assertEquals("Updated Notes", plan.getNotes());
        verify(planRepo).save(plan);
    }

    @Test
    void updatePlanThrowsNotFoundWhenPlanDoesNotExist() {
        UUID planId = UUID.randomUUID();
        when(planRepo.findById(planId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.updatePlan(
                        "coach@example.com",
                        planId,
                        new UpdateWorkoutPlanRequest("Updated Title", "Updated Notes")
                )
        );

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Plan not found", exception.getReason());
    }

    @Test
    void updatePlanThrowsNotFoundForDifferentCoachOwnership() {
        UUID planId = UUID.randomUUID();
        WorkoutPlan plan = WorkoutPlan.builder()
                .id(planId)
                .memberId(UUID.randomUUID())
                .coachEmail("other-coach@example.com")
                .title("Old Title")
                .notes("Old Notes")
                .createdAt(Instant.now())
                .build();

        when(planRepo.findById(planId)).thenReturn(Optional.of(plan));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.updatePlan(
                        "coach@example.com",
                        planId,
                        new UpdateWorkoutPlanRequest("Updated Title", "Updated Notes")
                )
        );

        assertEquals(NOT_FOUND, exception.getStatusCode());
        assertEquals("Plan not found", exception.getReason());
    }
}
