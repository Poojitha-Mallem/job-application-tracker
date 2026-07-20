package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.dto.StatsResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.User;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public StatsResponse getSummary(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long total = applicationRepository.countByUserId(user.getId());

        Map<String, Long> countByStatus = new HashMap<>();
        for (Object[] row : applicationRepository.countGroupedByStatus(user.getId())) {
            Application.Status status = (Application.Status) row[0];
            Long count = (Long) row[1];
            countByStatus.put(status.name(), count);
        }

        long responded = total - countByStatus.getOrDefault("APPLIED", 0L);
        long interviewed = countByStatus.getOrDefault("INTERVIEWING", 0L)
                + countByStatus.getOrDefault("OFFER", 0L);

        double responseRate = total == 0 ? 0.0 : (responded * 100.0) / total;
        double interviewRate = total == 0 ? 0.0 : (interviewed * 100.0) / total;

        return new StatsResponse(total, countByStatus, roundToTwoDecimals(responseRate), roundToTwoDecimals(interviewRate));
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}