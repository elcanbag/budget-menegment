package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    List<ExpenseCategory> findByUserId(Long userId);
}
