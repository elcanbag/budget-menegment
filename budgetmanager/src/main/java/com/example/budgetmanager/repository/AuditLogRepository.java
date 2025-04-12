package com.example.budgetmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.budgetmanager.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
