package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.budgetmanager.model.User;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }
    @PutMapping("/{username}")
    public ResponseEntity<User> updateProfile(@PathVariable String username, @Valid @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateProfile(username, userDTO);
        return ResponseEntity.ok(updatedUser);
    }


}
