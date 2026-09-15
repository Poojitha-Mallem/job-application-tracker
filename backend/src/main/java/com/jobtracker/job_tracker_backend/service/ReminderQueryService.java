package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.dto.ReminderResponse;
import com.jobtracker.job_tracker_backend.entity.Reminder;
import com.jobtracker.job_tracker_backend.entity.User;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ReminderRepository;
import com.jobtracker.job_tracker_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderQueryService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public List<ReminderResponse> getActiveReminders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return reminderRepository.findByApplicationUserIdAndSentFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void dismiss(String userEmail, Long reminderId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Reminder reminder = reminderRepository.findByIdAndApplicationUserId(reminderId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found"));

        reminder.setSent(true);
        reminderRepository.save(reminder);
    }

    private ReminderResponse toResponse(Reminder reminder) {
        return new ReminderResponse(
                reminder.getId(),
                reminder.getApplication().getId(),
                reminder.getApplication().getCompanyName(),
                reminder.getMessage(),
                reminder.isSent(),
                reminder.getCreatedAt()
        );
    }
}