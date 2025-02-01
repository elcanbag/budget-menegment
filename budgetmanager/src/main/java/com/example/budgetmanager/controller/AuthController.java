package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        String result = userService.registerUser(userDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> user = userService.findByUsername(userDTO.getUsername());

        if (user.isPresent() && user.get().getPassword().equals(userDTO.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }
}
