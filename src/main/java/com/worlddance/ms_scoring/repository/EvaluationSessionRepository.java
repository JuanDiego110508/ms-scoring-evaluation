package com.worlddance.ms_scoring.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.worlddance.ms_scoring.entity.EvaluationSession;

@Repository
public interface EvaluationSessionRepository extends MongoRepository<EvaluationSession, String> {
    
    Optional<EvaluationSession> findByEventIdAndModalityId(String eventId, String modalityId);
}
