package com.worlddance.ms_scoring.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.worlddance.ms_scoring.enums.ResultStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "results")
@CompoundIndex(
    name = "uk_event_modality_enrollment",
    def = "{ 'eventId': 1, 'modalityId': 1, 'enrollmentId': 1 }",
    unique = true
)
public class Result {

    @Id
    private String id;

    @NotBlank(message = "El ID del evento es obligatorio.")
    @Indexed
    private String eventId;

    @NotBlank(message = "El ID de la modalidad es obligatorio.")
    @Indexed
    private String modalityId;

    @NotBlank(message = "El ID de la inscripción es obligatorio.")
    @Indexed
    private String enrollmentId;

    private Double finalScore = 0.0;

    @Min(value = 1, message = "La posicion debe ser mayor o igual a 1.")
    private Integer ranking;

    private ResultStatus status;

    private Instant publishedAt;

    @CreatedDate
    private Instant createdAt;
}
