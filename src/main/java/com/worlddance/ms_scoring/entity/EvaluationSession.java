package com.worlddance.ms_scoring.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.worlddance.ms_scoring.enums.EvaluationSessionStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evaluation_sessions")
public class EvaluationSession {
    
    @Id
    private String id;

    @NotBlank
    @Indexed
    private String eventId;

    @NotBlank
    @Indexed
    private String modalityId;

    /**
    * Cantidad de evaluaciones esperadas.
    * Se calcula como: participantes aprobados × jurados asignados.
    */
    private Integer expectedEvaluations;

    private Integer completedEvaluations;

    private Integer expectedJudges;

    private EvaluationSessionStatus status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
