package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IncomeRecordRepository extends JpaRepository<IncomeRecord, Long> {

    List<IncomeRecord> findByUser(User user);

    Optional<IncomeRecord> findByIdAndUser(Long id, User user);
}
