package com.worlddance.ms_scoring.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriterionScoreRequest {
    
    @NotBlank(message = "El nombre del criterio es obligatorio.")
    private String criterionName;

    @NotNull(message = "El porcentaje del criterio es obligatorio.")
    @DecimalMin(value = "0.0", message = "El porcentaje no puede negativo.")
    @DecimalMax(value = "100.0", message = "El porcentaje no puede superar 100.")
    private Double percentage;

    @NotNull(message = "La calificación del criterio es obligatoria.")
    @DecimalMin(value = "0.0", message = "La calificacion no puede ser negativa.")
    private Double score;
}
