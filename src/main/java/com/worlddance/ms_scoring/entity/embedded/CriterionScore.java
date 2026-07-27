package com.worlddance.ms_scoring.entity.embedded;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriterionScore {
    
    @NotBlank(message = "El nombre del criterio es obligatorio.")
    private String criterionName;

    @Min(value = 0, message = "El porcentaje del criterio debe ser mayor o igual a 0.")
    @Max(value = 100, message = "El porcentaje del criterio no puede ser mayor a 100.")
    private Double percentage;

    @DecimalMin(value = "0.0", message = "La puntuación debe ser mayor o igual a 0.")
    @DecimalMax(value = "100.0", message = "La puntuación no puede ser mayor a 100")
    private Double score;

}
