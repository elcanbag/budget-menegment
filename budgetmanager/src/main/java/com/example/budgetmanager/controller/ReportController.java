package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ReportSummaryDTO;
import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.service.EmailService;
import com.example.budgetmanager.service.ReportExportService;
import com.example.budgetmanager.service.ReportService;
import jakarta.mail.MessagingException;
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
    private final IncomeRecordRepository incomeRecordRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public ReportController(ReportService reportService, ExpenseRecordRepository expenseRecordRepository, ReportExportService reportExportService, IncomeRecordRepository incomeRecordRepository, EmailService emailService, UserRepository userRepository) {
        this.reportService = reportService;
        this.expenseRecordRepository = expenseRecordRepository;
        this.reportExportService = reportExportService;
        this.incomeRecordRepository = incomeRecordRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
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

    @GetMapping("/export/incomes/pdf")
    public ResponseEntity<InputStreamResource> exportIncomesToPDF(Authentication auth) {
        String username = auth.getName();
        List<IncomeRecord> incomes = incomeRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportIncomesToPDF(incomes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=incomes.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/export/incomes/excel")
    public ResponseEntity<InputStreamResource> exportIncomesToExcel(Authentication auth) throws IOException {
        String username = auth.getName();
        List<IncomeRecord> incomes = incomeRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportIncomesToExcel(incomes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=incomes.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/email/expenses/pdf")
    public ResponseEntity<String> emailExpensesPdf(@RequestParam String email, Authentication auth) throws IOException, MessagingException {
        String username = auth.getName();
        List<ExpenseRecord> expenses = expenseRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportExpensesToPDF(expenses);
        byte[] data = stream.readAllBytes();
        emailService.sendEmailWithAttachment(email, "Your Expense Report (PDF)", "Please find attached your expense report in PDF format.", data, "expenses.pdf", "application/pdf");
        return ResponseEntity.ok("Expense PDF emailed successfully");
    }

    @GetMapping("/email/expenses/excel")
    public ResponseEntity<String> emailExpensesExcel(@RequestParam String email, Authentication auth) throws IOException, MessagingException {
        String username = auth.getName();
        List<ExpenseRecord> expenses = expenseRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportExpensesToExcel(expenses);
        byte[] data = stream.readAllBytes();
        emailService.sendEmailWithAttachment(email, "Your Expense Report (Excel)", "Please find attached your expense report in Excel format.", data, "expenses.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity.ok("Expense Excel emailed successfully");
    }

    @GetMapping("/email/incomes/pdf")
    public ResponseEntity<String> emailIncomesPdf(@RequestParam String email, Authentication auth) throws IOException, MessagingException {
        String username = auth.getName();
        List<IncomeRecord> incomes = incomeRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportIncomesToPDF(incomes);
        byte[] data = stream.readAllBytes();
        emailService.sendEmailWithAttachment(email, "Your Income Report (PDF)", "Please find attached your income report in PDF format.", data, "incomes.pdf", "application/pdf");
        return ResponseEntity.ok("Income PDF emailed successfully");
    }

    @GetMapping("/email/incomes/excel")
    public ResponseEntity<String> emailIncomesExcel(@RequestParam String email, Authentication auth) throws IOException, MessagingException {
        String username = auth.getName();
        List<IncomeRecord> incomes = incomeRecordRepository.findByUserUsername(username);
        ByteArrayInputStream stream = reportExportService.exportIncomesToExcel(incomes);
        byte[] data = stream.readAllBytes();
        emailService.sendEmailWithAttachment(email, "Your Income Report (Excel)", "Please find attached your income report in Excel format.", data, "incomes.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity.ok("Income Excel emailed successfully");
    }
}
