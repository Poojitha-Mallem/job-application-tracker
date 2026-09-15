package com.jobtracker.job_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReminderResponse {
    private Long id;
    private Long applicationId;
    private String companyName;
    private String message;
    private boolean sent;
    private LocalDateTime createdAt;
}