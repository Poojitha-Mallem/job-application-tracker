package com.jobtracker.job_tracker_backend.repository;

import com.jobtracker.job_tracker_backend.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByApplicationId(Long applicationId);
}