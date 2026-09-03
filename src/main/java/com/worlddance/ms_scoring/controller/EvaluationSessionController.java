package com.worlddance.ms_scoring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.worlddance.ms_scoring.dto.request.OpenEvaluationSessionRequest;
import com.worlddance.ms_scoring.dto.response.EvaluationSessionResponse;
import com.worlddance.ms_scoring.service.EvaluationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scoring/events/{eventId}/modalities/{modalityId}/session")
@RequiredArgsConstructor
public class EvaluationSessionController {

    private final EvaluationService evaluationService;

    @PostMapping("/open")
    public ResponseEntity<EvaluationSessionResponse> openEvaluationSession(
        @PathVariable String eventId,
        @PathVariable String modalityId,
        @Valid @RequestBody OpenEvaluationSessionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(evaluationService.openEvaluationSession(eventId, modalityId, request));
    }

    @GetMapping
    public ResponseEntity<EvaluationSessionResponse> getEvaluationSession(
        @PathVariable String eventId,
        @PathVariable String modalityId) {

        return ResponseEntity.ok(evaluationService.getEvaluationSession(eventId, modalityId));
    }

    @PatchMapping("/close")
    public ResponseEntity<Void> closeEvaluationSession(
        @PathVariable String eventId,
        @PathVariable String modalityId,
        @RequestParam String organizerId) {

        evaluationService.closeEvaluationSession(eventId, modalityId, organizerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/publish")
    public ResponseEntity<Void> publishResults(
        @PathVariable String eventId,
        @PathVariable String modalityId,
        @RequestParam String organizerId) {

        evaluationService.publishResults(eventId, modalityId, organizerId);
        return ResponseEntity.noContent().build();
    }
}