package com.example.budgetmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseRecordDTO {
    private Long id;
    private Double amount;
    private String description;
    private Long categoryId;
    private Long subCategoryId;
}
