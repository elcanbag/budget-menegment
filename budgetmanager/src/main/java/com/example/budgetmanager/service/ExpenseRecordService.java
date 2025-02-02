package com.example.budgetmanager.service;

import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseRecordService {

    private final ExpenseRecordRepository expenseRecordRepository;
    private final UserRepository userRepository; // Eksik olan userRepository eklendi

    public ExpenseRecordService(ExpenseRecordRepository expenseRecordRepository, UserRepository userRepository) {
        this.expenseRecordRepository = expenseRecordRepository;
        this.userRepository = userRepository;
    }

    public List<ExpenseRecord> getRecordsByAuthenticatedUser(Authentication authentication) {
        return expenseRecordRepository.findByUserUsername(authentication.getName());
    }

    public ExpenseRecord getRecordByIdAndAuthenticatedUser(Long id, Authentication authentication) {
        return expenseRecordRepository.findByIdAndUserUsername(id, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Expense record not found"));
    }

    public ExpenseRecord addRecord(ExpenseRecord expenseRecord, Authentication authentication) {
        String username = authentication.getName();

        // Mevcut kullanıcıyı veritabanından al
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kullanıcıyı ExpenseRecord'a ekle
        expenseRecord.setUser(user);

        return expenseRecordRepository.save(expenseRecord);
    }

    public ExpenseRecord updateRecord(Long id, ExpenseRecord updatedRecord, Authentication authentication) {
        ExpenseRecord record = getRecordByIdAndAuthenticatedUser(id, authentication);
        record.setAmount(updatedRecord.getAmount());
        record.setDate(updatedRecord.getDate());
        record.setCategory(updatedRecord.getCategory());
        return expenseRecordRepository.save(record);
    }

    public void deleteRecord(Long id, Authentication authentication) {
        ExpenseRecord record = getRecordByIdAndAuthenticatedUser(id, authentication);
        expenseRecordRepository.delete(record);
    }
}
