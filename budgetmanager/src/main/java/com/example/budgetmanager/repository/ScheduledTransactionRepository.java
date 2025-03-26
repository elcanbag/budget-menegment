package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.ScheduledTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, Long> {
    List<ScheduledTransaction> findByActiveTrueAndNextExecutionDateBefore(LocalDateTime dateTime);
    List<ScheduledTransaction> findByUserUsername(String username);
}
