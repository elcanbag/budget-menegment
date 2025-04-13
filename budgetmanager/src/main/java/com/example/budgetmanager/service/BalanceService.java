package com.example.budgetmanager.service;

import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final IncomeRecordRepository incomeRecordRepository;
    private final ExpenseRecordRepository expenseRecordRepository;

    public double calculateBalanceUntil(LocalDate date, User user) {
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        List<IncomeRecord> incomes = incomeRecordRepository.findByUserAndDateLessThanEqual(user, endOfDay);
        List<ExpenseRecord> expenses = expenseRecordRepository.findByUserAndDateLessThanEqual(user, endOfDay);
        double totalIncome = incomes.stream().mapToDouble(IncomeRecord::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(ExpenseRecord::getAmount).sum();
        return totalIncome - totalExpense;
    }
}
