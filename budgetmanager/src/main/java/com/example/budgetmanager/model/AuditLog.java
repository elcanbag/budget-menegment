package com.example.budgetmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String action;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String details;
    private LocalDateTime timestamp;
}
