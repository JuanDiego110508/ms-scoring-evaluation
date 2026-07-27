package com.worlddance.ms_scoring.dto.response;

import java.time.Instant;

import com.worlddance.ms_scoring.enums.ResultStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {

    private String id;

    private String eventId;

    private String modalityId;

    private String enrollmentId;

    /**
     * Obtener de enrollment service
     */
    private String participantName;

    private Double finalScore;

    private Integer ranking;

    private ResultStatus status;

    private Instant publishedAt;

    private Instant createdAt;

}