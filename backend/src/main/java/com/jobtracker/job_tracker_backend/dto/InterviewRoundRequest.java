package com.jobtracker.job_tracker_backend.dto;

import com.jobtracker.job_tracker_backend.entity.InterviewRound;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewRoundRequest {

    @NotNull(message = "Round type is required")
    private InterviewRound.RoundType roundType;

    private LocalDateTime scheduledAt;

    private InterviewRound.RoundStatus status;

    private String feedback;

    private InterviewRound.Result result;
}