package com.worlddance.ms_scoring.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.world_dance.wd_lib_common.dto.UserEventRoleResponseDto;
import com.world_dance.wd_lib_common.enums.EventRole;
import com.worlddance.ms_scoring.client.EnrollmentClient;
import com.worlddance.ms_scoring.dto.request.CreateEvaluationRequest;
import com.worlddance.ms_scoring.dto.request.CriterionScoreRequest;
import com.worlddance.ms_scoring.dto.request.UpdateEvaluationRequest;
import com.worlddance.ms_scoring.dto.response.EvaluationResponse;
import com.worlddance.ms_scoring.dto.response.ResultResponse;
import com.worlddance.ms_scoring.entity.Evaluation;
import com.worlddance.ms_scoring.entity.EvaluationSession;
import com.worlddance.ms_scoring.entity.Result;
import com.worlddance.ms_scoring.entity.embedded.CriterionScore;
import com.worlddance.ms_scoring.enums.EvaluationSessionStatus;
import com.worlddance.ms_scoring.enums.ResultStatus;
import com.worlddance.ms_scoring.exception.BadRequestException;
import com.worlddance.ms_scoring.exception.EvaluationClosedException;
import com.worlddance.ms_scoring.exception.ResourceNotFoundException;
import com.worlddance.ms_scoring.repository.EvaluationRepository;
import com.worlddance.ms_scoring.repository.EvaluationSessionRepository;
import com.worlddance.ms_scoring.repository.ResultRepository;
import com.worlddance.ms_scoring.service.EvaluationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationSessionRepository evaluationSessionRepository;
    private final ResultRepository resultRepository;
    private final EnrollmentClient enrollmentClient; // <- nuevo


    private static final double MAX_SCORE = 100.0;

    /**
     * Metodos publicos de la interfaz EvaluationService implementados en esta
     * clase.
     */
    @Override
    public EvaluationResponse registerEvaluation(String eventId, String modalityId, String enrollmentId, String userId,
            CreateEvaluationRequest request) {

        validateSessionOpen(eventId, modalityId);

        validateScores(request.getScores());

        /** Obtener el rol del usuario en el evento desde Enrollment */
        UserEventRoleResponseDto userEventRole = enrollmentClient.getUserEventRole(
                Long.parseLong(eventId), Long.parseLong(userId));

        if (userEventRole.getRoleInEvent() != EventRole.JURY) {
            throw new BadRequestException("Solo un jurado puede registrar evaluaciones para este evento.");
        }

        String userEventRoleId = String.valueOf(userEventRole.getId());

        if (evaluationRepository.existsByEventIdAndModalityIdAndEnrollmentIdAndUserEventRoleId(eventId, modalityId,
                enrollmentId, userEventRoleId)) {
            throw new BadRequestException("El jurado ya registro una evaluacion para este participante.");
        }

        Evaluation evaluation = buildEvaluation(eventId, modalityId, enrollmentId, userEventRoleId, request);

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        updateEvaluationSession(savedEvaluation);

        calculateParticipantResult(eventId, modalityId, enrollmentId);
        return buildResponse(savedEvaluation);
    }

    @Override
    public EvaluationResponse updateEvaluation(String evaluationId, String userId, UpdateEvaluationRequest request) {
        Evaluation evaluation = findEvaluationById(evaluationId);

        /**
         * Validar que el userId corresponda al userEventRoleId,
         * obtenerlo desde Enrollment Service.
         */

        validateSessionOpen(evaluation.getEventId(), evaluation.getModalityId());

        validateScores(request.getScores());
        List<CriterionScore> scores = buildScores(request.getScores());

        evaluation.setScores(scores);
        evaluation.setTotalScore(calculateTotalScore(scores));
        evaluation.setObservations(request.getObservations());

        Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
        return buildResponse(updatedEvaluation);
    }

    @Override
    public EvaluationResponse getEvaluation(String evaluationId) {

        Evaluation evaluation = findEvaluationById(evaluationId);
        return buildResponse(evaluation);
    }

    @Override
    public void closeEvaluationSession(String eventId, String modalityId, String organizerId) {

        /*
         * Validar que organizerId corresponda a un ORGANIZER
         * consultando Event service
         */


        EvaluationSession session = findEvaluationSession(eventId, modalityId);

        session.setStatus(EvaluationSessionStatus.CLOSED);
        evaluationSessionRepository.save(session);
    }

    @Override
    public void publishResults(String eventId, String modalityId, String organizerId) {


        /*
         * Validar que organizerId corresponda a un ORGANIZER
         * consultando Event service
         */


        EvaluationSession session = evaluationSessionRepository.findByEventIdAndModalityId(eventId, modalityId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la sesión de evaluación."));

        if (session.getStatus() != EvaluationSessionStatus.CLOSED) {
            throw new BadRequestException("La modalidad debe estar cerrada antes de publicar resultados.");
        }

        List<Result> results = resultRepository.findByEventIdAndModalityIdAndStatusOrderByFinalScoreDesc(eventId,
                modalityId, ResultStatus.READY);

        for (Result result : results) {

            result.setStatus(ResultStatus.PUBLISHED);
            result.setPublishedAt(Instant.now());

            resultRepository.save(result);
        }

        session.setStatus(EvaluationSessionStatus.PUBLISHED);
        evaluationSessionRepository.save(session);
    }

    @Override
    public List<ResultResponse> getResultsByModality(String eventId, String modalityId) {

        List<Result> results = resultRepository.findByEventIdAndModalityIdAndStatusOrderByFinalScoreDesc(eventId,
                modalityId, ResultStatus.PUBLISHED);

        List<ResultResponse> response = new ArrayList<>();

        for (Result result : results) {
            response.add(buildResultResponse(result));
        }

        return response;
    }

    /**
     * Metodos auxiliares utilizados para la logica de negocio.
     */

    private void validateSessionOpen(String eventId, String modalityId) {

        EvaluationSession session = findEvaluationSession(eventId, modalityId);

        if (session.getStatus() == EvaluationSessionStatus.CLOSED) {
            throw new EvaluationClosedException(
                    "La modalidad ya fue cerrada.");
        }

        if (session.getStatus() == EvaluationSessionStatus.PUBLISHED) {
            throw new EvaluationClosedException(
                    "Los resultados ya fueron publicados.");
        }
    }

    private EvaluationSession findEvaluationSession(String eventId, String modalityId) {
        return evaluationSessionRepository.findByEventIdAndModalityId(eventId, modalityId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la sesión de evaluación."));
    }

    private void updateEvaluationSession(Evaluation evaluation) {

        EvaluationSession session = findEvaluationSession(evaluation.getEventId(),
                evaluation.getModalityId());

        Integer completed = session.getCompletedEvaluations();

        if (completed == null) {
            completed = 0;
        }

        session.setCompletedEvaluations(completed + 1);

        if (session.getCompletedEvaluations() >= session.getExpectedEvaluations()) {
            session.setStatus(EvaluationSessionStatus.CLOSED);
        }

        evaluationSessionRepository.save(session);
    }

    private void calculateParticipantResult(String eventId, String modalityId, String enrollmentId) {

        List<Evaluation> evaluations = evaluationRepository.findByEventIdAndModalityIdAndEnrollmentId(eventId,
                modalityId, enrollmentId);

        EvaluationSession session = evaluationSessionRepository.findByEventIdAndModalityId(eventId, modalityId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la sesión de evaluación."));

        if (evaluations.isEmpty()) {
            return;
        }

        double total = 0.0;

        for (Evaluation evaluation : evaluations) {
            total += evaluation.getTotalScore();
        }

        double finalScore = total / evaluations.size();

        Result result = resultRepository
                .findByEventIdAndModalityIdAndEnrollmentId(eventId, modalityId, enrollmentId)
                .orElseGet(Result::new);

        result.setEventId(eventId);
        result.setModalityId(modalityId);
        result.setEnrollmentId(enrollmentId);

        if (evaluations.size() == session.getExpectedJudges()) {
            result.setFinalScore(finalScore);
            result.setStatus(ResultStatus.READY);
        } else {
            result.setFinalScore(null);
            result.setStatus(ResultStatus.PENDING);
        }

        resultRepository.save(result);
        generateRanking(eventId, modalityId);
    }

    private void generateRanking(String eventId, String modalityId) {

        List<Result> results = resultRepository.findByEventIdAndModalityIdAndStatusOrderByFinalScoreDesc(eventId,
                modalityId, ResultStatus.READY);

        int ranking = 1;

        for (Result result : results) {

            result.setRanking(ranking++);
            resultRepository.save(result);
        }
    }

    private Evaluation findEvaluationById(String evaluationId) {

        return evaluationRepository.findById(evaluationId).orElseThrow(
                () -> new ResourceNotFoundException("No se encontró la evaluación con ID: " + evaluationId));
    }

    private Double calculateTotalScore(List<CriterionScore> scores) {
        double totalScore = 0.0;

        for (CriterionScore criterion : scores) {
            totalScore += (criterion.getScore() * criterion.getPercentage()) / MAX_SCORE;
        }

        return totalScore;
    }

    private void validateScores(List<CriterionScoreRequest> scores) {

        if (scores == null || scores.isEmpty()) {
            throw new BadRequestException("Debe registrar al menos un criterio de evaluación.");
        }

        if (scores.size() > 20) {
            throw new BadRequestException("No es posible registrar más de 20 criterios.");
        }

        double totalPercentage = 0;

        Set<String> criterionNames = new HashSet<>();

        for (CriterionScoreRequest criterion : scores) {

            if (criterion.getCriterionName() == null || criterion.getCriterionName().isBlank()) {
                throw new BadRequestException("El nombre del criterio es obligatorio.");
            }

            if (!criterionNames.add(criterion.getCriterionName().trim().toLowerCase())) {
                throw new BadRequestException(
                        "El criterio '" + criterion.getCriterionName() + "' está repetido.");
            }

            if (criterion.getPercentage() <= 0) {
                throw new BadRequestException(
                        "El porcentaje del criterio '" + criterion.getCriterionName() + "' debe ser mayor a 0.");
            }

            if (criterion.getPercentage() > MAX_SCORE) {
                throw new BadRequestException(
                        "El porcentaje del criterio '" + criterion.getCriterionName() + "' no puede ser mayor a 100.");
            }

            if (criterion.getScore() < 0) {
                throw new BadRequestException(
                        "La calificacion del criterio '" + criterion.getCriterionName() + "' no puede ser negativa.");
            }

            if (criterion.getScore() > MAX_SCORE) {
                throw new BadRequestException(
                        "La calificacion del criterio '" + criterion.getCriterionName()
                                + "' no puede ser mayor a 100.");
            }

            totalPercentage += criterion.getPercentage();
        }

        if (Double.compare(totalPercentage, MAX_SCORE) != 0) {
            throw new BadRequestException(
                    "La suma de los porcentajes debe ser exactamente 100. Actualmente es: " + totalPercentage);
        }
    }

    private List<CriterionScore> buildScores(List<CriterionScoreRequest> requests) {

        List<CriterionScore> scores = new ArrayList<>();

        for (CriterionScoreRequest request : requests) {

            CriterionScore criterionScore = new CriterionScore();
            criterionScore.setCriterionName(request.getCriterionName().trim().replaceAll("\\s+", " "));
            criterionScore.setPercentage(request.getPercentage());
            criterionScore.setScore(request.getScore());

            scores.add(criterionScore);
        }
        return scores;
    }

    private Evaluation buildEvaluation(String eventId, String modalityId, String enrollmentId,
            String userEventRoleId, CreateEvaluationRequest request) {

        Evaluation evaluation = new Evaluation();

        evaluation.setEventId(eventId);
        evaluation.setModalityId(modalityId);
        evaluation.setEnrollmentId(enrollmentId);
        evaluation.setUserEventRoleId(userEventRoleId);

        List<CriterionScore> scores = buildScores(request.getScores());

        evaluation.setScores(scores);
        evaluation.setTotalScore(calculateTotalScore(scores));
        evaluation.setObservations(request.getObservations());

        return evaluation;
    }

    private EvaluationResponse buildResponse(Evaluation evaluation) {
        EvaluationResponse response = new EvaluationResponse();

        response.setEvaluationId(evaluation.getId());
        response.setEventId(evaluation.getEventId());
        response.setModalityId(evaluation.getModalityId());
        response.setEnrollmentId(evaluation.getEnrollmentId());
        response.setScores(evaluation.getScores());
        response.setTotalScore(evaluation.getTotalScore());
        response.setObservations(evaluation.getObservations());

        return response;
    }

    private ResultResponse buildResultResponse(Result result) {

        ResultResponse response = new ResultResponse();

        response.setId(result.getId());
        response.setEventId(result.getEventId());
        response.setModalityId(result.getModalityId());
        response.setEnrollmentId(result.getEnrollmentId());
        response.setFinalScore(result.getFinalScore());
        response.setRanking(result.getRanking());
        response.setStatus(result.getStatus());
        response.setPublishedAt(result.getPublishedAt());
        response.setCreatedAt(result.getCreatedAt());

        return response;
    }
}