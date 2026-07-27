package com.worlddance.ms_scoring.service;

import java.util.List;

import com.worlddance.ms_scoring.dto.request.CreateEvaluationRequest;
import com.worlddance.ms_scoring.dto.request.UpdateEvaluationRequest;
import com.worlddance.ms_scoring.dto.response.EvaluationResponse;
import com.worlddance.ms_scoring.dto.response.ResultResponse;
import com.worlddance.ms_scoring.entity.Result;

public interface EvaluationService {
    
    /**
     *  HU44 - Registrar puntaje. 
    */
    EvaluationResponse registerEvaluation(
        String eventId,
        String modalityId,
        String enrollmentId,
        String userId,
        CreateEvaluationRequest request
    );

    /**
     * HU46 - Editar puntaje. 
    */
    EvaluationResponse updateEvaluation(
        String evaluationId,
        String userId,
        UpdateEvaluationRequest request
    );

    /**
     * Consultar una evaluación. 
    */
    EvaluationResponse getEvaluation(
        String evaluationId
    );

    /**
     * HU47 - Cerrar evaluación.
     */
    void closeEvaluationSession(
        String eventId,
        String modalityId,
        String organizerId
    );

    /**
     * HU50 - Publicar Resultados
     */
    void publishResults(
        String eventId,
        String modalityId,
        String organizerId
    );

    /**
     * HU51 - Consultar resultados del evento
     */
    List<ResultResponse> getResultsByModality(
        String eventId,
        String modalityId
    );
}
