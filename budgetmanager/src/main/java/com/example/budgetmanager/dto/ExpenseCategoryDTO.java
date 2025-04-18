package com.example.budgetmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategoryDTO {
    private Long id;
    private String name;
    private Long parentId;

    public ExpenseCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
