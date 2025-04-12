package com.example.budgetmanager.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class FileAuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(FileAuditLogService.class);
    private static final String LOG_FILE_PATH = "audit.log";
    public void saveLog(String username, String action, String details) {
        String logEntry = String.format("[%s] Username: %s, Action: %s, Details: %s%n", LocalDateTime.now(), username, action, details);
        try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true)) {
            fw.write(logEntry);
        } catch (IOException e) {
            logger.error("Error writing audit log", e);
        }
    }
}
