package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.ExpenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecord, Long> {
    List<ExpenseRecord> findByUserUsername(String username);
    Optional<ExpenseRecord> findByIdAndUserUsername(Long id, String username);
}
