package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ReportSummaryDTO;
import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import com.example.budgetmanager.service.ReportExportService;
import com.example.budgetmanager.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final ExpenseRecordRepository expenseRecordRepository;
    private final ReportExportService reportExportService;

    public ReportController(ReportService reportService,
                            ExpenseRecordRepository expenseRecordRepository,
                            ReportExportService reportExportService) {
        this.reportService = reportService;
        this.expenseRecordRepository = expenseRecordRepository;
        this.reportExportService = reportExportService;
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

    @GetMapping("/export/expenses/excel")
    public ResponseEntity<InputStreamResource> exportExpensesToExcel(Authentication auth) throws IOException {
        String username = auth.getName();
        List<ExpenseRecord> expenses = expenseRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportExpensesToExcel(expenses);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expenses.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/export/expenses/pdf")
    public ResponseEntity<InputStreamResource> exportExpensesToPDF(Authentication auth) throws IOException {
        String username = auth.getName();
        List<ExpenseRecord> expenses = expenseRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportExpensesToPDF(expenses);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expenses.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(stream));
    }

}
