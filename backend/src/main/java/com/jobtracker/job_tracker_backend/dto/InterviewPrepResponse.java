package com.jobtracker.job_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InterviewPrepResponse {
    private String companyName;
    private String jobTitle;
    private List<String> technicalQuestions;
    private List<String> behavioralQuestions;
}