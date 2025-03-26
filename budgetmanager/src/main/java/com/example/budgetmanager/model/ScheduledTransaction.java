package com.example.budgetmanager.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Double amount;
    private String description;

    private Long categoryId;
    private Long subCategoryId;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_interval")
    private Interval transactionInterval;

    private LocalDateTime nextExecutionDate;
    private boolean active = true;

    public enum TransactionType {
        INCOME, EXPENSE
    }

    public enum Interval {
        DAILY, WEEKLY, MONTHLY
    }
}
