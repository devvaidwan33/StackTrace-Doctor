package com.stacktracedoctor.api.repository;

import com.stacktracedoctor.api.entity.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugReportRepository extends JpaRepository<BugReport, Long> {
    List<BugReport> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndErrorCategory(Long userId, String errorCategory);
}
