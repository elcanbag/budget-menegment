package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.ReportSummaryDTO;
import com.example.budgetmanager.dto.MonthlyReportDTO;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class ReportService {

    private final IncomeRecordRepository incomeRecordRepository;
    private final ExpenseRecordRepository expenseRecordRepository;

    public ReportService(IncomeRecordRepository incomeRecordRepository, ExpenseRecordRepository expenseRecordRepository) {
        this.incomeRecordRepository = incomeRecordRepository;
        this.expenseRecordRepository = expenseRecordRepository;
    }

    public ReportSummaryDTO getSummary(Authentication authentication) {
        String username = authentication.getName();
        Double totalIncome = incomeRecordRepository.getTotalIncomeByUsername(username);
        Double totalExpense = expenseRecordRepository.getTotalExpenseByUsername(username);
        ReportSummaryDTO dto = new ReportSummaryDTO();
        dto.setTotalIncome(totalIncome != null ? totalIncome : 0);
        dto.setTotalExpense(totalExpense != null ? totalExpense : 0);
        dto.setNet((totalIncome != null ? totalIncome : 0) - (totalExpense != null ? totalExpense : 0));
        return dto;
    }

    public List<MonthlyReportDTO> getMonthly(Authentication authentication) {
        String username = authentication.getName();
        List<MonthlyReportDTO> incomeList = incomeRecordRepository.getMonthlyIncomeByUsername(username);
        List<MonthlyReportDTO> expenseList = expenseRecordRepository.getMonthlyExpenseByUsername(username);
        Map<String, MonthlyReportDTO> map = new HashMap<>();
        for (MonthlyReportDTO m : incomeList) {
            map.put(m.getMonth(), m);
        }
        for (MonthlyReportDTO m : expenseList) {
            MonthlyReportDTO existing = map.getOrDefault(m.getMonth(), new MonthlyReportDTO());
            existing.setMonth(m.getMonth());
            existing.setExpense(m.getExpense());
            map.put(m.getMonth(), existing);
        }
        return new ArrayList<>(map.values());
    }

    public Map<String, Double> getExpenseByCategory(String username) {
        List<Object[]> results = expenseRecordRepository.getTotalExpenseGroupedByCategory(username);
        Map<String, Double> categoryMap = new HashMap<>();
        for (Object[] row : results) {
            String categoryName = (String) row[0];
            Double totalAmount = (Double) row[1];
            categoryMap.put(categoryName, totalAmount);
        }
        return categoryMap;
    }

    public Map<String, Double> getExpenseByCategory(String username, LocalDateTime start, LocalDateTime end) {
        List<Object[]> results = expenseRecordRepository.getExpenseByCategoryInDateRange(username, start, end);
        Map<String, Double> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Double) row[1]);
        }
        return map;
    }

    public Map<String, Double> getIncomeByCategory(String username, LocalDateTime start, LocalDateTime end) {
        List<Object[]> results = incomeRecordRepository.getIncomeByCategoryInDateRange(username, start, end);
        Map<String, Double> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((String) row[0], (Double) row[1]);
        }
        return map;
    }

    public Map<String, Double> getIncomeByCategory(String username) {
        return getIncomeByCategory(username, LocalDateTime.MIN, LocalDateTime.MAX);
    }


}
