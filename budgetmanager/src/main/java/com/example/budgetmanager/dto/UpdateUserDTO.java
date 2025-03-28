package com.example.budgetmanager.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String fullName;
    private String email;
    private String password;
}
