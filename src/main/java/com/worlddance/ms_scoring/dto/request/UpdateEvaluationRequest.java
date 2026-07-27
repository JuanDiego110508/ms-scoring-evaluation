package com.worlddance.ms_scoring.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEvaluationRequest {
    
    @Valid
    @NotEmpty(message = "Debe existir al menos un criterio de evaluación.")
    private List<CriterionScoreRequest> scores;

    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.")
    private String observations;
}
