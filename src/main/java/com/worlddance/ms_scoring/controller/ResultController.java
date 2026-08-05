package com.worlddance.ms_scoring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.worlddance.ms_scoring.dto.response.ResultResponse;
import com.worlddance.ms_scoring.service.EvaluationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/results/{eventId}/{modalityId}")
@RequiredArgsConstructor
public class ResultController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<ResultResponse>> getResultsByModality(
        @PathVariable String eventId,
        @PathVariable String modalityId) {

            return ResponseEntity.ok(evaluationService.getResultsByModality(eventId, modalityId));
        }
}