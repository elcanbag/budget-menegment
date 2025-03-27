package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ReportSummaryDTO;
import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDTO> getSummary(Authentication authentication) {
        return ResponseEntity.ok(reportService.getSummary(authentication));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyReportDTO>> getMonthly(Authentication authentication) {
        return ResponseEntity.ok(reportService.getMonthly(authentication));
    }

    @GetMapping("/expense-by-category")
    public ResponseEntity<Map<String, Double>> getExpenseByCategory(Authentication authentication) {
        return ResponseEntity.ok(reportService.getExpenseByCategory(authentication.getName()));
    }

    @GetMapping("/expense-by-category/range")
    public ResponseEntity<Map<String, Double>> getExpenseByCategoryInRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Authentication authentication) {
        return ResponseEntity.ok(reportService.getExpenseByCategory(authentication.getName(), start, end));
    }

    @GetMapping("/income-by-category")
    public ResponseEntity<Map<String, Double>> getIncomeByCategory(Authentication authentication) {
        return ResponseEntity.ok(reportService.getIncomeByCategory(authentication.getName()));
    }

    @GetMapping("/income-by-category/range")
    public ResponseEntity<Map<String, Double>> getIncomeByCategoryInRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Authentication authentication) {
        return ResponseEntity.ok(reportService.getIncomeByCategory(authentication.getName(), start, end));
    }
}
