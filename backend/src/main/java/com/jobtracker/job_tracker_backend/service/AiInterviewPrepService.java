package com.jobtracker.job_tracker_backend.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.jobtracker.job_tracker_backend.dto.InterviewPrepResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.exception.AccessDeniedException;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiInterviewPrepService {

    private final ApplicationRepository applicationRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public InterviewPrepResponse generateQuestions(String userEmail, Long applicationId) {
        Application application = getOwnedApplication(userEmail, applicationId);

        String prompt = buildPrompt(application);

        try {
            String rawResponse = callGeminiApi(prompt);
            return parseResponse(application, rawResponse);
        } catch (WebClientResponseException e) {
            throw new RuntimeException("The AI service is temporarily unavailable. Please try again in a moment.");
        }
    }

    private String buildPrompt(Application application) {
        return """
                You are an expert technical interviewer. Based on the following job details, generate exactly 5 technical interview questions and 5 behavioral interview questions relevant to this specific role.

                Company: %s
                Job Title: %s
                Job Description: %s

                Respond ONLY in this exact JSON format, with no extra text, no markdown code fences:
                {
                  "technicalQuestions": ["question1", "question2", "question3", "question4", "question5"],
                  "behavioralQuestions": ["question1", "question2", "question3", "question4", "question5"]
                }
                """.formatted(
                application.getCompanyName(),
                application.getJobTitle(),
                application.getJobDescription() != null ? application.getJobDescription() : "Not provided"
        );
    }

    private String callGeminiApi(String prompt) {
        WebClient webClient = webClientBuilder.build();

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        String fullUrl = apiUrl + "/" + model + ":generateContent?key=" + apiKey;

        JsonNode response = webClient.post()
                .uri(fullUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return response
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText();
    }

    private InterviewPrepResponse parseResponse(Application application, String rawResponse) {
        try {
            String cleaned = rawResponse.trim()
                    .replaceAll("^```json", "")
                    .replaceAll("^```", "")
                    .replaceAll("```$", "")
                    .trim();

            JsonNode json = objectMapper.readTree(cleaned);

            List<String> technicalQuestions = new ArrayList<>();
            json.path("technicalQuestions").forEach(node -> technicalQuestions.add(node.asText()));

            List<String> behavioralQuestions = new ArrayList<>();
            json.path("behavioralQuestions").forEach(node -> behavioralQuestions.add(node.asText()));

            return new InterviewPrepResponse(
                    application.getCompanyName(),
                    application.getJobTitle(),
                    technicalQuestions,
                    behavioralQuestions
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage());
        }
    }

    private Application getOwnedApplication(String userEmail, Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You do not have access to this application");
        }

        return application;
    }
}