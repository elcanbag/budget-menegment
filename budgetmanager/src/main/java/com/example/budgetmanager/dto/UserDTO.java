package com.example.budgetmanager.dto;

import com.example.budgetmanager.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserDTO {
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }

    public UserDTO() {

    }
}
