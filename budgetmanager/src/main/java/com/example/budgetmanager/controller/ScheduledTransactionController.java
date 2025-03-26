package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ScheduledTransactionDTO;
import com.example.budgetmanager.model.ScheduledTransaction;
import com.example.budgetmanager.service.ScheduledTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduled-transactions")
public class ScheduledTransactionController {

    private final ScheduledTransactionService scheduledTransactionService;

    public ScheduledTransactionController(ScheduledTransactionService scheduledTransactionService) {
        this.scheduledTransactionService = scheduledTransactionService;
    }


    @PostMapping
    public ResponseEntity<ScheduledTransaction> createScheduledTransaction(@RequestBody ScheduledTransactionDTO dto, Authentication authentication) {
        ScheduledTransaction scheduledTransaction = ScheduledTransaction.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .subCategoryId(dto.getSubCategoryId())
                .transactionType(dto.getTransactionType())

                .transactionInterval(dto.getTransactionInterval())
                .nextExecutionDate(dto.getNextExecutionDate())
                .active(true)
                .user((com.example.budgetmanager.model.User) authentication.getPrincipal())
                .build();

        ScheduledTransaction saved = scheduledTransactionService.createScheduledTransaction(scheduledTransaction);
        return ResponseEntity.ok(saved);
    }


    @GetMapping
    public ResponseEntity<List<ScheduledTransaction>> getAllScheduledTransactions(Authentication authentication) {
        List<ScheduledTransaction> transactions = scheduledTransactionService.getScheduledTransactionsByUser(authentication);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTransaction> getScheduledTransactionById(@PathVariable Long id, Authentication authentication) {
        ScheduledTransaction transaction = scheduledTransactionService.getScheduledTransactionById(id, authentication);
        return ResponseEntity.ok(transaction);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ScheduledTransaction> updateScheduledTransaction(@PathVariable Long id,
                                                                           @RequestBody ScheduledTransactionDTO dto,
                                                                           Authentication authentication) {
        ScheduledTransaction updatedTransaction = ScheduledTransaction.builder()
                .id(id)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .subCategoryId(dto.getSubCategoryId())
                .transactionType(dto.getTransactionType())
                .transactionInterval(dto.getTransactionInterval())
                .nextExecutionDate(dto.getNextExecutionDate())
                .active(dto.isActive())
                .user((com.example.budgetmanager.model.User) authentication.getPrincipal())
                .build();

        ScheduledTransaction transaction = scheduledTransactionService.updateScheduledTransaction(id, updatedTransaction, authentication);
        return ResponseEntity.ok(transaction);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledTransaction(@PathVariable Long id, Authentication authentication) {
        scheduledTransactionService.deleteScheduledTransaction(id, authentication);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/execute")
    public ResponseEntity<Void> executeScheduledTransactions() {
        scheduledTransactionService.executeScheduledTransactions();
        return ResponseEntity.ok().build();
    }
}
