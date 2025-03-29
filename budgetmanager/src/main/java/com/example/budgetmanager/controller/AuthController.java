package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.LoginDTO;
import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.model.VerificationToken;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.repository.VerificationTokenRepository;
import com.example.budgetmanager.service.EmailService;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    public AuthController(UserService userService, UserRepository userRepository,
                          VerificationTokenRepository tokenRepository, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userService.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409).body("User already exists!");
        }
        User newUser = userService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully with username: " + newUser.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<User> user = userService.findByUsername(loginDTO.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(loginDTO.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password!");
        }
    }

    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return ResponseEntity.status(404).body("Email not found");

        User user = userOptional.get();


        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);


        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);
        VerificationToken vt = new VerificationToken(null, token, user, expiration);
        tokenRepository.save(vt);


        String resetLink = "http://localhost/auth/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Reset your password", "Click to reset: " + resetLink);

        return ResponseEntity.ok("Reset link sent to your email");
    }


    @GetMapping("/verify-token")
    public ResponseEntity<Boolean> verifyToken(@RequestParam String token) {
        return ResponseEntity.ok(tokenRepository.findByToken(token)
                .filter(t -> t.getExpirationDate().isAfter(LocalDateTime.now()))
                .isPresent());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        VerificationToken vt = tokenRepository.findByToken(token)
                .filter(t -> t.getExpirationDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        User user = vt.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);
        tokenRepository.delete(vt);

        return ResponseEntity.ok("Password updated successfully");
    }
}
