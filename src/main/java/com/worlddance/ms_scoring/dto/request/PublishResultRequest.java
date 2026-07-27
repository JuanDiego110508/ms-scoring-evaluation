package com.worlddance.ms_scoring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishResultRequest {
    
    @NotBlank(message = "El identificador del evento es obligatorio.")
    private String eventId;

    @NotBlank(message = "El identificador de la modalidad es obligatorio.")
    private String modalityId;
}
