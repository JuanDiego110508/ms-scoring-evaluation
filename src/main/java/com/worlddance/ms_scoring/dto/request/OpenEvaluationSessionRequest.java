package com.worlddance.ms_scoring.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenEvaluationSessionRequest {

    @NotNull(message = "La cantidad esperada de evaluaciones es obligatoria.")
    @Min(value = 1, message = "La cantidad esperada de evaluaciones debe ser al menos 1.")
    private Integer expectedEvaluations;

    @NotNull(message = "La cantidad esperada de jurados es obligatoria.")
    @Min(value = 1, message = "La cantidad esperada de jurados debe ser al menos 1.")
    private Integer expectedJudges;

    private String organizerId;
}
