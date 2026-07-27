package com.worlddance.ms_scoring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.worlddance.ms_scoring.entity.Evaluation;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
    
    Optional<Evaluation> findByEventIdAndModalityIdAndEnrollmentIdAndUserEventRoleId(
        String eventId,
        String modalityId,
        String enrollmentId,
        String userEventRoleId);

    List<Evaluation> findByEventIdAndModalityId(
        String eventId,
        String modalityId);

    List<Evaluation> findByEventIdAndModalityIdAndEnrollmentId(
        String eventId,
        String modalityId,
        String enrollmentId);

    boolean existsByEventIdAndModalityIdAndEnrollmentIdAndUserEventRoleId(
        String eventId,
        String modalityId,
        String enrollmentId,
        String userEventRoleId);
}
