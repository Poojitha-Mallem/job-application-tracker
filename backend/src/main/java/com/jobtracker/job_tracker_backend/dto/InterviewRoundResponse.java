package com.jobtracker.job_tracker_backend.dto;

import com.jobtracker.job_tracker_backend.entity.InterviewRound;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InterviewRoundResponse {

    private Long id;
    private InterviewRound.RoundType roundType;
    private LocalDateTime scheduledAt;
    private InterviewRound.RoundStatus status;
    private String feedback;
    private InterviewRound.Result result;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InterviewRoundResponse fromEntity(InterviewRound round) {
        return new InterviewRoundResponse(
                round.getId(),
                round.getRoundType(),
                round.getScheduledAt(),
                round.getStatus(),
                round.getFeedback(),
                round.getResult(),
                round.getCreatedAt(),
                round.getUpdatedAt()
        );
    }
}