package com.coach.workout.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "exercises")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Exercise {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID dayId;
    @Column(nullable = false)
    private String name;
    private String notes;
}
