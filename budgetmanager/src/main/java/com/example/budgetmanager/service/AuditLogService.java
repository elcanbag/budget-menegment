package com.example.budgetmanager.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.example.budgetmanager.model.AuditLog;
import com.example.budgetmanager.repository.AuditLogRepository;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    public AuditLog saveLog(String username, String action, String details) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        return auditLogRepository.save(log);
    }
}
