package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.dto.ReminderResponse;
import com.jobtracker.job_tracker_backend.service.ReminderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderQueryService reminderQueryService;

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getActiveReminders(Authentication authentication) {
        return ResponseEntity.ok(reminderQueryService.getActiveReminders(authentication.getName()));
    }

    @PatchMapping("/{id}/dismiss")
    public ResponseEntity<Void> dismiss(Authentication authentication, @PathVariable Long id) {
        reminderQueryService.dismiss(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}