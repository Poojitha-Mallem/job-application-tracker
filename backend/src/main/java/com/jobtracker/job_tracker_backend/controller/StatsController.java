package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.dto.StatsResponse;
import com.jobtracker.job_tracker_backend.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/summary")
    public ResponseEntity<StatsResponse> getSummary(Authentication authentication) {
        return ResponseEntity.ok(statsService.getSummary(authentication.getName()));
    }
}