package com.stacktracedoctor.api.controller;

import com.stacktracedoctor.api.entity.BugReport;
import com.stacktracedoctor.api.service.BugAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bugs")
@CrossOrigin(origins = "*")
public class BugReportController {

    private final BugAnalysisService bugAnalysisService;

    public BugReportController(BugAnalysisService bugAnalysisService) {
        this.bugAnalysisService = bugAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<BugReport> analyzeBug(@RequestBody Map<String, String> payload) {
        Long userId = Long.parseLong(payload.get("userId"));
        String title = payload.getOrDefault("title", "Untitled Bug");
        String language = payload.get("language");
        String buggyCode = payload.get("buggyCode");
        String errorLog = payload.getOrDefault("errorLog", "Direct Code Inspection (No stack trace provided)");
        if (errorLog == null || errorLog.isBlank()) errorLog = "Direct Code Inspection (No stack trace provided)";
        if (title == null || title.isBlank()) title = language + " Code Analysis";

        BugReport saved = bugAnalysisService.analyzeAndSaveBug(userId, title, language, buggyCode, errorLog);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<BugReport>> getHistory(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(bugAnalysisService.getUserBugHistory(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBug(@PathVariable("id") Long id) {
        bugAnalysisService.deleteBugById(id);
        return ResponseEntity.noContent().build();
    }
}
