package com.worlddance.ms_scoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.worlddance.ms_scoring.dto.request.CreateEvaluationRequest;
import com.worlddance.ms_scoring.dto.request.UpdateEvaluationRequest;
import com.worlddance.ms_scoring.dto.response.EvaluationResponse;
import com.worlddance.ms_scoring.service.EvaluationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events/{eventId}/modalities/{modalityId}/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/enrollments/{enrollmentId}")
    public ResponseEntity<EvaluationResponse> registerEvaluation(

        @PathVariable String eventId,
        @PathVariable String modalityId,
        @PathVariable String enrollmentId,
        @RequestParam String userId,

        @Valid
        @RequestBody CreateEvaluationRequest request) {

            return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.registerEvaluation(
                eventId, modalityId, enrollmentId, userId, request));
        }

    @PutMapping("/{evaluationId}")
    public ResponseEntity<EvaluationResponse> updateEvaluation(

        @PathVariable String evaluationId,
        @RequestParam String userId,

        @Valid
        @RequestBody UpdateEvaluationRequest request) {

            return ResponseEntity.ok(evaluationService.updateEvaluation(evaluationId, userId, request));
        }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<EvaluationResponse> getEvaluation(@PathVariable String evaluationId) {
        
        return ResponseEntity.ok(evaluationService.getEvaluation(evaluationId));
    }
}
