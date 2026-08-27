package com.worlddance.ms_scoring.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.world_dance.wd_lib_common.dto.UserEventRoleResponseDto;

@FeignClient(name = "ms-enrollment", path = "/api/v1/enrollments")
public interface EnrollmentClient {

    @GetMapping("/events/{eventId}/users/{userId}/role")
    UserEventRoleResponseDto getUserEventRole(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId);
}