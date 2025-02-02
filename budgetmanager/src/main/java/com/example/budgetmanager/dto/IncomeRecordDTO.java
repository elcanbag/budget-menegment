package com.example.budgetmanager.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncomeRecordDTO {
    private Long userId;
    private Long categoryId; // Kategori ID ekleniyor
    private Double amount;
    private LocalDateTime date;
}
