package com.example.budgetmanager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSubCategoryDTO {
    private Long id;
    private String name;
    private Long parentCategoryId;
}
