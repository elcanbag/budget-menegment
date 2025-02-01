package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.IncomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {
    Optional<IncomeCategory> findByName(String name);
}
