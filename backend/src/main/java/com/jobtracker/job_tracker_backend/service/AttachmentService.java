package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.Attachment;
import com.jobtracker.job_tracker_backend.exception.AccessDeniedException;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final ApplicationRepository applicationRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Attachment upload(String userEmail, Long applicationId, Attachment.FileType fileType, MultipartFile file) {
        Application application = getOwnedApplication(userEmail, applicationId);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID() + "_" + originalFileName;
            Path targetPath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetPath);

            Attachment attachment = Attachment.builder()
                    .application(application)
                    .fileName(originalFileName)
                    .filePath(targetPath.toString())
                    .fileType(fileType)
                    .build();

            return attachmentRepository.save(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    public List<Attachment> getByApplication(String userEmail, Long applicationId) {
        getOwnedApplication(userEmail, applicationId);
        return attachmentRepository.findByApplicationId(applicationId);
    }

    public void delete(String userEmail, Long attachmentId) {
        Attachment attachment = getOwnedAttachment(userEmail, attachmentId);

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from disk: " + e.getMessage());
        }

        attachmentRepository.delete(attachment);
    }

    private Application getOwnedApplication(String userEmail, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this application");
        }

        return application;
    }

    private Attachment getOwnedAttachment(String userEmail, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        if (!attachment.getApplication().getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this attachment");
        }

        return attachment;
    }
}