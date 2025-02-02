package com.example.budgetmanager.controller;

import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.service.ExpenseRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense-records")
public class ExpenseRecordController {

    private final ExpenseRecordService expenseRecordService;

    public ExpenseRecordController(ExpenseRecordService expenseRecordService) {
        this.expenseRecordService = expenseRecordService;
    }

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
    public ResponseEntity<ExpenseRecord> addExpenseRecord(@RequestBody ExpenseRecord expenseRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(expenseRecordService.addRecord(expenseRecord, authentication));
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
}
