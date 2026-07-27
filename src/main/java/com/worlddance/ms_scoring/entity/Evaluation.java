package com.worlddance.ms_scoring.entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.worlddance.ms_scoring.entity.embedded.CriterionScore;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "evaluations")
@CompoundIndex(
    name = "uk_event_modality_enrollment_jury",
    def = "{ 'eventId': 1, 'modalityId': 1, 'enrollmentId': 1, 'userEventRoleId': 1 }",
    unique = true
)
public class Evaluation {
    
    @Id
    private String id;
    
    @NotBlank(message = "El ID del rol del usuario en el evento es obligatorio.")
    @Indexed
    private String userEventRoleId;

    @NotBlank(message = "El ID del evento es obligatorio.")
    @Indexed
    private String eventId;

    @NotBlank(message = "El ID de la modalidad es obligatorio.")
    @Indexed
    private String modalityId;

    @NotBlank(message = "El ID de la inscripción es obligatorio.")
    @Indexed
    private String enrollmentId;

    @Valid
    @NotEmpty(message = "Debe existir al menos un criterio de evaluación.")
    private List<CriterionScore> scores;

    private Double totalScore = 0.0;

    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.")
    private String observations;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
