package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.dto.UpdateUserDTO;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.budgetmanager.model.User;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public ResponseEntity<UserDTO> getUserSettings(Authentication auth) {
        User user = userService.getByUsername(auth.getName());
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/settings")
    public ResponseEntity<Void> updateUserSettings(@RequestBody UpdateUserDTO dto, Authentication auth) {
        userService.updateUser(auth.getName(), dto);
        return ResponseEntity.ok().build();
    }



}
