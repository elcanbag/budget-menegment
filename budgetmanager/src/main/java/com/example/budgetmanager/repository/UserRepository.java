package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // 🔥 Bu metodu ekleyerek hatayı çözüyoruz!
}
