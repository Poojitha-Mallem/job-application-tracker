package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.dto.InterviewRoundRequest;
import com.jobtracker.job_tracker_backend.dto.InterviewRoundResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.InterviewRound;
import com.jobtracker.job_tracker_backend.exception.AccessDeniedException;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.InterviewRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewRoundService {

    private final InterviewRoundRepository interviewRoundRepository;
    private final ApplicationRepository applicationRepository;

    public InterviewRoundResponse create(String userEmail, Long applicationId, InterviewRoundRequest request) {
        Application application = getOwnedApplication(userEmail, applicationId);

        InterviewRound round = InterviewRound.builder()
                .application(application)
                .roundType(request.getRoundType())
                .scheduledAt(request.getScheduledAt())
                .status(request.getStatus() != null ? request.getStatus() : InterviewRound.RoundStatus.SCHEDULED)
                .feedback(request.getFeedback())
                .result(request.getResult() != null ? request.getResult() : InterviewRound.Result.PENDING)
                .build();

        InterviewRound saved = interviewRoundRepository.save(round);
        return InterviewRoundResponse.fromEntity(saved);
    }

    public List<InterviewRoundResponse> getByApplication(String userEmail, Long applicationId) {
        getOwnedApplication(userEmail, applicationId);

        return interviewRoundRepository.findByApplicationIdOrderByScheduledAtAsc(applicationId)
                .stream()
                .map(InterviewRoundResponse::fromEntity)
                .toList();
    }

    public InterviewRoundResponse update(String userEmail, Long roundId, InterviewRoundRequest request) {
        InterviewRound round = getOwnedRound(userEmail, roundId);

        round.setRoundType(request.getRoundType());
        round.setScheduledAt(request.getScheduledAt());
        if (request.getStatus() != null) {
            round.setStatus(request.getStatus());
        }
        round.setFeedback(request.getFeedback());
        if (request.getResult() != null) {
            round.setResult(request.getResult());
        }

        InterviewRound saved = interviewRoundRepository.save(round);
        return InterviewRoundResponse.fromEntity(saved);
    }

    public void delete(String userEmail, Long roundId) {
        InterviewRound round = getOwnedRound(userEmail, roundId);
        interviewRoundRepository.delete(round);
    }

    private Application getOwnedApplication(String userEmail, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this application");
        }

        return application;
    }

    private InterviewRound getOwnedRound(String userEmail, Long roundId) {
        InterviewRound round = interviewRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview round not found"));

        if (!round.getApplication().getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this interview round");
        }

        return round;
    }
}