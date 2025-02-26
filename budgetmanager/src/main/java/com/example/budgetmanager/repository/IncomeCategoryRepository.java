package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    List<IncomeCategory> findByUser(User user);
}
