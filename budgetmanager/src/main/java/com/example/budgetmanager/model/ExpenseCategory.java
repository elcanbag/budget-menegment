package com.example.budgetmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_categories")
public class ExpenseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ExpenseCategory parentCategory;

    @JsonIgnoreProperties({
            "password", "email", "active", "enabled",
            "accountNonLocked", "authorities",
            "credentialsNonExpired", "accountNonExpired"
    })
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
