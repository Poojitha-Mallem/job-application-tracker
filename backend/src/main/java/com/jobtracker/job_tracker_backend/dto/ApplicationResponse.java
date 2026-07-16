package com.jobtracker.job_tracker_backend.dto;

import com.jobtracker.job_tracker_backend.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private String jobDescription;
    private Application.Status status;
    private LocalDate appliedDate;
    private String source;
    private Double salaryExpectation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ApplicationResponse fromEntity(Application app) {
        return new ApplicationResponse(
                app.getId(),
                app.getCompanyName(),
                app.getJobTitle(),
                app.getJobDescription(),
                app.getStatus(),
                app.getAppliedDate(),
                app.getSource(),
                app.getSalaryExpectation(),
                app.getCreatedAt(),
                app.getUpdatedAt()
        );
    }
}