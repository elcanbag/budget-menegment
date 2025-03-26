package com.example.budgetmanager.dto;

import lombok.Data;

@Data
public class ReportSummaryDTO {
    private double totalIncome;
    private double totalExpense;
    private double net;
}
