package com.worlddance.ms_scoring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.worlddance.ms_scoring.service.EvaluationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/events/{eventId}/modalities/{modalityId}")
@RequiredArgsConstructor
public class EvaluationSessionController {

    private final EvaluationService evaluationService;

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
