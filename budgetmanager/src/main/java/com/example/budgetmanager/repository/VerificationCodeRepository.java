package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByUser(User user);
    Optional<VerificationCode> findByUserAndCode(User user, String code);
    void deleteByUser(User user);
}
