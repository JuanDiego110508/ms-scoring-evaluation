package com.worlddance.ms_scoring.dto.response;

import java.time.Instant;

import com.worlddance.ms_scoring.enums.EvaluationSessionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationSessionResponse {

    private String id;

    private String eventId;

    private String modalityId;

    private Integer expectedEvaluations;

    private Integer completedEvaluations;

    private Integer expectedJudges;

    private EvaluationSessionStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}
