package com.jobtracker.job_tracker_backend.repository;

import com.jobtracker.job_tracker_backend.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);

    List<Reminder> findByApplicationUserIdAndSentFalseOrderByCreatedAtDesc(Long userId);

    boolean existsByApplicationIdAndSentFalse(Long applicationId);

    Optional<Reminder> findByIdAndApplicationUserId(Long id, Long userId);
}