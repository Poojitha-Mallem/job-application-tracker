package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.dto.InterviewRoundRequest;
import com.jobtracker.job_tracker_backend.dto.InterviewRoundResponse;
import com.jobtracker.job_tracker_backend.service.InterviewRoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    @PostMapping("/api/applications/{applicationId}/rounds")
    public ResponseEntity<InterviewRoundResponse> create(
            Authentication authentication,
            @PathVariable Long applicationId,
            @Valid @RequestBody InterviewRoundRequest request
    ) {
        InterviewRoundResponse response = interviewRoundService.create(authentication.getName(), applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/applications/{applicationId}/rounds")
    public ResponseEntity<List<InterviewRoundResponse>> getByApplication(
            Authentication authentication,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(interviewRoundService.getByApplication(authentication.getName(), applicationId));
    }

    @PutMapping("/api/rounds/{roundId}")
    public ResponseEntity<InterviewRoundResponse> update(
            Authentication authentication,
            @PathVariable Long roundId,
            @Valid @RequestBody InterviewRoundRequest request
    ) {
        return ResponseEntity.ok(interviewRoundService.update(authentication.getName(), roundId, request));
    }

    @DeleteMapping("/api/rounds/{roundId}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long roundId) {
        interviewRoundService.delete(authentication.getName(), roundId);
        return ResponseEntity.noContent().build();
    }
}