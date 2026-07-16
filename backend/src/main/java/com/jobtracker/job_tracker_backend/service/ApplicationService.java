package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.dto.ApplicationRequest;
import com.jobtracker.job_tracker_backend.dto.ApplicationResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.User;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.exception.AccessDeniedException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationResponse create(String userEmail, ApplicationRequest request) {
        User user = getUserByEmail(userEmail);

        Application application = Application.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .status(request.getStatus() != null ? request.getStatus() : Application.Status.APPLIED)
                .appliedDate(request.getAppliedDate())
                .source(request.getSource())
                .salaryExpectation(request.getSalaryExpectation())
                .build();

        Application saved = applicationRepository.save(application);
        return ApplicationResponse.fromEntity(saved);
    }

    public Page<ApplicationResponse> search(String userEmail, String companyName, Application.Status status, Pageable pageable) {
        User user = getUserByEmail(userEmail);
        return applicationRepository
                .searchApplications(user.getId(), companyName, status, pageable)
                .map(ApplicationResponse::fromEntity);
    }

    public ApplicationResponse getById(String userEmail, Long id) {
        Application application = getOwnedApplication(userEmail, id);
        return ApplicationResponse.fromEntity(application);
    }

    public ApplicationResponse update(String userEmail, Long id, ApplicationRequest request) {
        Application application = getOwnedApplication(userEmail, id);

        application.setCompanyName(request.getCompanyName());
        application.setJobTitle(request.getJobTitle());
        application.setJobDescription(request.getJobDescription());
        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }
        application.setAppliedDate(request.getAppliedDate());
        application.setSource(request.getSource());
        application.setSalaryExpectation(request.getSalaryExpectation());

        Application saved = applicationRepository.save(application);
        return ApplicationResponse.fromEntity(saved);
    }

    public ApplicationResponse updateStatus(String userEmail, Long id, Application.Status status) {
        Application application = getOwnedApplication(userEmail, id);
        application.setStatus(status);
        Application saved = applicationRepository.save(application);
        return ApplicationResponse.fromEntity(saved);
    }

    public void delete(String userEmail, Long id) {
        Application application = getOwnedApplication(userEmail, id);
        applicationRepository.delete(application);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Application getOwnedApplication(String userEmail, Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this application");
        }

        return application;
    }
}