package com.worlddance.ms_scoring.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.worlddance.ms_scoring.entity.EvaluationSession;

public interface EvaluationSessionRepository extends MongoRepository<EvaluationSession, String> {
    
    Optional<EvaluationSession> findByEventIdAndModalityId(String eventId, String modalityId);
}
