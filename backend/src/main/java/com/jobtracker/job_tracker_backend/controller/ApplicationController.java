package com.jobtracker.job_tracker_backend.controller;

import com.jobtracker.job_tracker_backend.dto.ApplicationRequest;
import com.jobtracker.job_tracker_backend.dto.ApplicationResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            Authentication authentication,
            @Valid @RequestBody ApplicationRequest request
    ) {
        ApplicationResponse response = applicationService.create(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ApplicationResponse>> search(
            Authentication authentication,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Application.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ApplicationResponse> result = applicationService.search(
                authentication.getName(), companyName, status, pageable
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(authentication.getName(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequest request
    ) {
        return ResponseEntity.ok(applicationService.update(authentication.getName(), id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam Application.Status status
    ) {
        return ResponseEntity.ok(applicationService.updateStatus(authentication.getName(), id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        applicationService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}