package com.jobtracker.job_tracker_backend.repository;

import com.jobtracker.job_tracker_backend.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);

    boolean existsByApplicationIdAndSentFalse(Long applicationId);
}