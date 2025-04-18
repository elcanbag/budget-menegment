package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ExpenseRecordDTO;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.service.ExpenseRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/expense-records")
@RequiredArgsConstructor
public class ExpenseRecordController {

    private final ExpenseRecordService expenseRecordService;

    @GetMapping
    public ResponseEntity<List<ExpenseRecord>> getUserExpenseRecords() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(expenseRecordService.getRecordsByAuthenticatedUser(authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseRecord> getExpenseRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(expenseRecordService.getRecordByIdAndAuthenticatedUser(id, authentication));
    }

    @PostMapping
    public ResponseEntity<String> addExpenseRecord(@RequestBody ExpenseRecordDTO dto, Authentication authentication) {
        expenseRecordService.addExpenseRecord(dto, authentication);
        return ResponseEntity.ok("Expense record added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseRecord> updateExpenseRecord(@PathVariable Long id, @RequestBody ExpenseRecord updatedRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(expenseRecordService.updateRecord(id, updatedRecord, authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        expenseRecordService.deleteRecord(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ExpenseRecord>> filterExpenseRecords(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Authentication authentication) {
        return ResponseEntity.ok(expenseRecordService.getRecordsByDateRange(authentication, start, end));
    }

}
