package com.example.budgetmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseRecordDTO {
    private Long categoryId;
    private Double amount;
    private LocalDateTime date;
}
