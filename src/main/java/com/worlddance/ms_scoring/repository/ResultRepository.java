package com.worlddance.ms_scoring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.worlddance.ms_scoring.entity.Result;
import com.worlddance.ms_scoring.enums.ResultStatus;

public interface ResultRepository extends MongoRepository<Result, String> {
    
    Optional<Result> findByEventIdAndModalityIdAndEnrollmentId(String eventId, String modalityId, String enrollmentId);

    List<Result> findByEventIdAndModalityIdAndStatusOrderByFinalScoreDesc(String eventId, String modalityId, ResultStatus status);
}
