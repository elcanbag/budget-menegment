package com.example.budgetmanager.service;

import org.springframework.stereotype.Service;

@Service
public class CompositeAuditLogService {
    private final AuditLogService auditLogService;
    private final FileAuditLogService fileAuditLogService;

    public CompositeAuditLogService(AuditLogService auditLogService, FileAuditLogService fileAuditLogService) {
        this.auditLogService = auditLogService;
        this.fileAuditLogService = fileAuditLogService;
    }

    public void saveLog(String username, String action, String details) {
        auditLogService.saveLog(username, action, details);
        fileAuditLogService.saveLog(username, action, details);
    }
}
