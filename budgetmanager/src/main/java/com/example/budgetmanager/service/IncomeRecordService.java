package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.IncomeRecordDTO;
import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.IncomeCategoryRepository;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeRecordService {

    private final IncomeRecordRepository incomeRecordRepository;
    private final UserRepository userRepository;

    public IncomeRecordService(IncomeRecordRepository incomeRecordRepository, UserRepository userRepository) {
        this.incomeRecordRepository = incomeRecordRepository;
        this.userRepository = userRepository;
    }


    private User getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public List<IncomeRecord> getRecordsByAuthenticatedUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return incomeRecordRepository.findByUser(user);
    }


    public IncomeRecord getRecordByIdAndAuthenticatedUser(Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));
    }


    @Autowired
    private IncomeCategoryRepository incomeCategoryRepository;

    public IncomeRecord addRecord(IncomeRecordDTO dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        IncomeCategory category = incomeCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeRecord record = new IncomeRecord();
        record.setUser(user);
        record.setCategory(category);
        record.setAmount(dto.getAmount());
        record.setDate(dto.getDate());

        return incomeRecordRepository.save(record);
    }



    public IncomeRecord updateRecord(Long id, IncomeRecord updatedRecord, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        IncomeRecord record = incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));

        record.setAmount(updatedRecord.getAmount());
        record.setCategory(updatedRecord.getCategory());
        record.setDate(updatedRecord.getDate());

        return incomeRecordRepository.save(record);
    }


    public void deleteRecord(Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        IncomeRecord record = incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));
        incomeRecordRepository.delete(record);
    }

    public List<IncomeRecord> getRecordsByDateRange(Authentication authentication, LocalDateTime start, LocalDateTime end) {
        return incomeRecordRepository.findByUserUsernameAndDateBetween(authentication.getName(), start, end);
    }

}
