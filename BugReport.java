package com.stacktracedoctor.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bug_reports")
public class BugReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "language", nullable = false, length = 50)
    private String language;

    @Column(name = "error_category", nullable = false, length = 80)
    private String errorCategory;

    @Column(name = "buggy_code", nullable = false, columnDefinition = "TEXT")
    private String buggyCode;

    @Column(name = "error_log", nullable = false, columnDefinition = "TEXT")
    private String errorLog;

    @Column(name = "root_cause", nullable = false, columnDefinition = "TEXT")
    private String rootCause;

    @Column(name = "fixed_code", nullable = false, columnDefinition = "TEXT")
    private String fixedCode;

    @Column(name = "viva_note", nullable = false, columnDefinition = "TEXT")
    private String vivaNote;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public BugReport() {}

    public BugReport(Long userId, String title, String language, String errorCategory,
                     String buggyCode, String errorLog, String rootCause, String fixedCode, String vivaNote) {
        this.userId = userId;
        this.title = title;
        this.language = language;
        this.errorCategory = errorCategory;
        this.buggyCode = buggyCode;
        this.errorLog = errorLog;
        this.rootCause = rootCause;
        this.fixedCode = fixedCode;
        this.vivaNote = vivaNote;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getErrorCategory() { return errorCategory; }
    public void setErrorCategory(String errorCategory) { this.errorCategory = errorCategory; }

    public String getBuggyCode() { return buggyCode; }
    public void setBuggyCode(String buggyCode) { this.buggyCode = buggyCode; }

    public String getErrorLog() { return errorLog; }
    public void setErrorLog(String errorLog) { this.errorLog = errorLog; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getFixedCode() { return fixedCode; }
    public void setFixedCode(String fixedCode) { this.fixedCode = fixedCode; }

    public String getVivaNote() { return vivaNote; }
    public void setVivaNote(String vivaNote) { this.vivaNote = vivaNote; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
