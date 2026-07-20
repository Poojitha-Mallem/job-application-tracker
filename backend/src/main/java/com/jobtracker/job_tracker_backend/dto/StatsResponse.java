package com.jobtracker.job_tracker_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class StatsResponse {
    private long totalApplications;
    private Map<String, Long> countByStatus;
    private double responseRate;
    private double interviewRate;
}