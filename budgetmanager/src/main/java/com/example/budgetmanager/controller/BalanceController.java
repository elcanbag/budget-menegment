package com.example.budgetmanager.controller;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;
    private final UserRepository userRepository;

    @GetMapping("/balance")
    public double getBalanceUntilDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        return balanceService.calculateBalanceUntil(date, user);
    }
}
