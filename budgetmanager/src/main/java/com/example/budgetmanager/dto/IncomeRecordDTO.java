package com.example.budgetmanager.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncomeRecordDTO {
    private Long userId;
    private Long categoryId;
    private Double amount;
    private LocalDateTime date;
}
