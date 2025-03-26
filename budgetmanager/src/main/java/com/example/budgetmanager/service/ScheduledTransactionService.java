package com.example.budgetmanager.service;

import com.example.budgetmanager.model.ScheduledTransaction;
import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.model.ExpenseSubCategory;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.ScheduledTransaction.Interval;
import com.example.budgetmanager.model.ScheduledTransaction.TransactionType;
import com.example.budgetmanager.repository.ScheduledTransactionRepository;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import com.example.budgetmanager.repository.IncomeCategoryRepository;
import com.example.budgetmanager.repository.ExpenseCategoryRepository;
import com.example.budgetmanager.repository.ExpenseSubCategoryRepository;
import com.example.budgetmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledTransactionService {

    private final ScheduledTransactionRepository scheduledTransactionRepository;
    private final IncomeRecordRepository incomeRecordRepository;
    private final ExpenseRecordRepository expenseRecordRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseSubCategoryRepository expenseSubCategoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void executeScheduledTransactions() {
        List<ScheduledTransaction> transactions = scheduledTransactionRepository.findByActiveTrueAndNextExecutionDateBefore(LocalDateTime.now());
        for (ScheduledTransaction tx : transactions) {
            if (tx.getTransactionType() == TransactionType.INCOME) {
                executeIncomeTransaction(tx);
            } else if (tx.getTransactionType() == TransactionType.EXPENSE) {
                executeExpenseTransaction(tx);
            }
            updateNextExecutionDate(tx);
        }
    }

    private void executeIncomeTransaction(ScheduledTransaction tx) {
        IncomeCategory incomeCategory = incomeCategoryRepository.findById(tx.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Scheduled transaction için gelir kategorisi bulunamadı"));
        IncomeRecord incomeRecord = new IncomeRecord();
        incomeRecord.setAmount(tx.getAmount());
        incomeRecord.setDate(LocalDateTime.now());
        incomeRecord.setUser(tx.getUser());
        incomeRecord.setCategory(incomeCategory);
        incomeRecordRepository.save(incomeRecord);
    }

    private void executeExpenseTransaction(ScheduledTransaction tx) {
        ExpenseCategory expenseCategory = expenseCategoryRepository.findById(tx.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Scheduled transaction için gider kategorisi bulunamadı"));
        ExpenseRecord expenseRecord = new ExpenseRecord();
        expenseRecord.setAmount(tx.getAmount());
        expenseRecord.setDescription(tx.getDescription());
        expenseRecord.setDate(LocalDateTime.now());
        expenseRecord.setUser(tx.getUser());
        expenseRecord.setCategory(expenseCategory);
        if (tx.getSubCategoryId() != null) {
            ExpenseSubCategory subCategory = expenseSubCategoryRepository.findById(tx.getSubCategoryId())
                    .orElseThrow(() -> new RuntimeException("Scheduled transaction için gider alt kategorisi bulunamadı"));
            expenseRecord.setSubCategory(subCategory);
        }
        expenseRecordRepository.save(expenseRecord);
    }

    private void updateNextExecutionDate(ScheduledTransaction tx) {
        LocalDateTime nextDate = tx.getNextExecutionDate();
        Interval interval = tx.getTransactionInterval();
        switch (interval) {
            case DAILY:
                nextDate = nextDate.plusDays(1);
                break;
            case WEEKLY:
                nextDate = nextDate.plusWeeks(1);
                break;
            case MONTHLY:
                nextDate = nextDate.plusMonths(1);
                break;
            default:
                throw new RuntimeException("Desteklenmeyen periyot");
        }
        tx.setNextExecutionDate(nextDate);
        scheduledTransactionRepository.save(tx);
    }

    public ScheduledTransaction createScheduledTransaction(ScheduledTransaction transaction) {
        return scheduledTransactionRepository.save(transaction);
    }

    public List<ScheduledTransaction> getScheduledTransactionsByUser(Authentication authentication) {
        String username = authentication.getName();
        return scheduledTransactionRepository.findByUserUsername(username);
    }

    public ScheduledTransaction getScheduledTransactionById(Long id, Authentication authentication) {
        String username = authentication.getName();
        Optional<ScheduledTransaction> optional = scheduledTransactionRepository.findById(id);
        if(optional.isEmpty()) {
            throw new RuntimeException("Scheduled transaction not found");
        }
        ScheduledTransaction transaction = optional.get();
        if(!transaction.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }
        return transaction;
    }

    public ScheduledTransaction updateScheduledTransaction(Long id, ScheduledTransaction updatedTransaction, Authentication authentication) {
        ScheduledTransaction existing = getScheduledTransactionById(id, authentication);
        existing.setAmount(updatedTransaction.getAmount());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setCategoryId(updatedTransaction.getCategoryId());
        existing.setSubCategoryId(updatedTransaction.getSubCategoryId());
        existing.setTransactionType(updatedTransaction.getTransactionType());
        existing.setTransactionInterval(updatedTransaction.getTransactionInterval());
        existing.setNextExecutionDate(updatedTransaction.getNextExecutionDate());
        existing.setActive(updatedTransaction.isActive());
        return scheduledTransactionRepository.save(existing);
    }

    public void deleteScheduledTransaction(Long id, Authentication authentication) {
        ScheduledTransaction existing = getScheduledTransactionById(id, authentication);
        scheduledTransactionRepository.delete(existing);
    }
}
