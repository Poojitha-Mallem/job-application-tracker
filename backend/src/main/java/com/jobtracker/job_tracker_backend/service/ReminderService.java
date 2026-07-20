package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.Reminder;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ApplicationRepository applicationRepository;
    private final ReminderRepository reminderRepository;

    private static final int STALE_THRESHOLD_DAYS = 7;

    @Scheduled(cron = "0 0 9 * * *")
    public void generateStaleApplicationReminders() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(STALE_THRESHOLD_DAYS);
        List<Application> staleApplications = applicationRepository.findStaleApplications(cutoff);

        for (Application application : staleApplications) {
            boolean alreadyReminded = reminderRepository.existsByApplicationIdAndSentFalse(application.getId());

            if (!alreadyReminded) {
                Reminder reminder = Reminder.builder()
                        .application(application)
                        .message(String.format(
                                "No update on your %s application at %s in over %d days — consider following up.",
                                application.getJobTitle(), application.getCompanyName(), STALE_THRESHOLD_DAYS
                        ))
                        .build();

                reminderRepository.save(reminder);
            }
        }
    }
}