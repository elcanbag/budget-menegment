package com.example.budgetmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "expense_records")
public class ExpenseRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategory category;

    @ManyToOne
    @JoinColumn(name = "expense_subcategory_id", nullable = true)
    private ExpenseSubCategory subCategory;

    @ManyToOne
    @JsonIgnoreProperties({
            "password", "email", "active", "enabled",
            "accountNonLocked", "authorities",
            "credentialsNonExpired", "accountNonExpired"
    })
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime date;
}
