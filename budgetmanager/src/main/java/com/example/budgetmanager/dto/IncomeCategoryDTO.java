package com.example.budgetmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeCategoryDTO {
    private Long id;
    private String name;

    public IncomeCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
