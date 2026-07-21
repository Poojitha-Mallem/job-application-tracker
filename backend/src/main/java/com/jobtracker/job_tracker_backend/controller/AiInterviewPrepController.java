package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.dto.InterviewPrepResponse;
import com.jobtracker.job_tracker_backend.service.AiInterviewPrepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiInterviewPrepController {

    private final AiInterviewPrepService aiInterviewPrepService;

    @GetMapping("/api/applications/{applicationId}/interview-prep")
    public ResponseEntity<InterviewPrepResponse> generateQuestions(
            Authentication authentication,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(aiInterviewPrepService.generateQuestions(authentication.getName(), applicationId));
    }
}