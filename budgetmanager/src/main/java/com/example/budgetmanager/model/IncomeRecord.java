package com.example.budgetmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "income_records")
public class IncomeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private IncomeCategory category;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnoreProperties({
            "password", "email", "active", "enabled",
            "accountNonLocked", "authorities",
            "credentialsNonExpired", "accountNonExpired"
    })
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
