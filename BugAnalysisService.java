package com.stacktracedoctor.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stacktracedoctor.api.entity.BugReport;
import com.stacktracedoctor.api.repository.BugReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BugAnalysisService {

    private final BugReportRepository bugReportRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public BugAnalysisService(BugReportRepository bugReportRepository, RestTemplate restTemplate) {
        this.bugReportRepository = bugReportRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public BugReport analyzeAndSaveBug(Long userId, String title, String language, String buggyCode, String errorLog) {
        String prompt = buildStructuredPrompt(language, buggyCode, errorLog);
        Map<String, String> parsed = callGeminiRestApi(prompt, buggyCode);

        BugReport report = new BugReport(
                userId,
                title,
                language,
                parsed.get("errorCategory"),
                buggyCode,
                errorLog,
                parsed.get("rootCause"),
                parsed.get("fixedCode"),
                parsed.get("vivaNote")
        );
        return bugReportRepository.save(report);
    }

    public List<BugReport> getUserBugHistory(Long userId) {
        return bugReportRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deleteBugById(Long id) {
        bugReportRepository.deleteById(id);
    }

    private String buildStructuredPrompt(String language, String buggyCode, String errorLog) {
        return "You are an expert Computer Science mentor. Analyze this " + language + " bug.\n"
                + "Return ONLY a valid JSON object with keys: "
                + ""errorCategory" (e.g. NULL_POINTER_EXCEPTION, SPRING_BEAN_ERROR, PATH_VARIABLE_ERROR, SQL_DATABASE_ERROR), "
                + ""rootCause" (2 clear sentences), "
                + ""fixedCode" (corrected code snippet), "
                + ""vivaNote" (1-2 sentences explaining the concept for an exam viva).\n\n"
                + "BUGGY CODE:\n" + buggyCode + "\n\n"
                + "ERROR LOG:\n" + errorLog;
    }

    private Map<String, String> callGeminiRestApi(String promptText, String fallbackCode) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);

            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", promptText);

            Map<String, Object> contentItem = new HashMap<>();
            contentItem.put("parts", List.of(textPart));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(contentItem));

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    geminiApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String rawText = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            String cleanedJson = rawText.replace("```json", "").replace("```", "").trim();
            JsonNode resultNode = objectMapper.readTree(cleanedJson);

            return Map.of(
                    "errorCategory", resultNode.path("errorCategory").asText("RUNTIME_ERROR"),
                    "rootCause", resultNode.path("rootCause").asText("Root cause analyzed by Gemini."),
                    "fixedCode", resultNode.path("fixedCode").asText(fallbackCode),
                    "vivaNote", resultNode.path("vivaNote").asText("Review exception handling and types.")
            );
        } catch (Exception e) {
            return Map.of(
                    "errorCategory", "RUNTIME_ERROR",
                    "rootCause", "Fallback analysis (check Gemini API key): " + e.getMessage(),
                    "fixedCode", fallbackCode,
                    "vivaNote", "Ensure gemini.api.key is configured in application.properties."
            );
        }
    }
}
