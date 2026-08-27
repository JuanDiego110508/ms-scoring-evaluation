package com.worlddance.ms_scoring.dto.response;

import java.time.Instant;
import java.util.List;

import com.worlddance.ms_scoring.entity.embedded.CriterionScore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponse {

    private String id;

    private String evaluationId;

    private String eventId;

    private String modalityId;

    private String enrollmentId;

    private Double totalScore;

    private List<CriterionScore> scores;

    private String observations;

    private Instant createdAt;

    private Instant updatedAt;

}