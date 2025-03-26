package com.example.budgetmanager.dto;

import com.example.budgetmanager.model.ScheduledTransaction.Interval;
import com.example.budgetmanager.model.ScheduledTransaction.TransactionType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduledTransactionDTO {
    private Long id;
    private Double amount;
    private String description;
    private Long categoryId;
    private Long subCategoryId;
    private TransactionType transactionType;
    private Interval transactionInterval;
    private LocalDateTime nextExecutionDate;
    private boolean active;
}
