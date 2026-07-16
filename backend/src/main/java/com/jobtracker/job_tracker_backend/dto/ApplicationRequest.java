package com.jobtracker.job_tracker_backend.dto;

import com.jobtracker.job_tracker_backend.entity.Application;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    private String jobDescription;

    private Application.Status status;

    private LocalDate appliedDate;

    private String source;

    private Double salaryExpectation;
}