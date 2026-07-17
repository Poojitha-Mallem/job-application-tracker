package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.entity.Attachment;
import com.jobtracker.job_tracker_backend.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(value = "/api/applications/{applicationId}/attachments", consumes = "multipart/form-data")
    public ResponseEntity<Attachment> upload(
            Authentication authentication,
            @PathVariable Long applicationId,
            @RequestParam("fileType") Attachment.FileType fileType,
            @RequestParam("file") MultipartFile file
    ) {
        Attachment attachment = attachmentService.upload(authentication.getName(), applicationId, fileType, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
    }

    @GetMapping("/api/applications/{applicationId}/attachments")
    public ResponseEntity<List<Attachment>> getByApplication(
            Authentication authentication,
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(attachmentService.getByApplication(authentication.getName(), applicationId));
    }

    @DeleteMapping("/api/attachments/{attachmentId}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long attachmentId) {
        attachmentService.delete(authentication.getName(), attachmentId);
        return ResponseEntity.noContent().build();
    }
}