package com.example.budgetmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_subcategories")
public class ExpenseSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "expense_category_id", nullable = false)
    private ExpenseCategory parentCategory;

    @ManyToOne
    @JsonIgnoreProperties({
            "password", "email", "active", "enabled",
            "accountNonLocked", "authorities",
            "credentialsNonExpired", "accountNonExpired"
    })
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}