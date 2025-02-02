package com.example.budgetmanager.service;

import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.IncomeRecordRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    // 🔹 Kimlik doğrulamaya göre kullanıcıyı al
    private User getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 🔹 Kullanıcının tüm gelir kayıtlarını getir
    public List<IncomeRecord> getRecordsByAuthenticatedUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return incomeRecordRepository.findByUser(user);
    }

    // 🔹 Kullanıcının belirli bir gelir kaydını getir
    public IncomeRecord getRecordByIdAndAuthenticatedUser(Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));
    }

    // 🔹 Gelir kaydı ekle
    public IncomeRecord addRecord(IncomeRecord incomeRecord, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        incomeRecord.setUser(user);
        return incomeRecordRepository.save(incomeRecord);
    }

    // 🔹 Gelir kaydını güncelle
    public IncomeRecord updateRecord(Long id, IncomeRecord updatedRecord, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        IncomeRecord record = incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));

        record.setAmount(updatedRecord.getAmount());
        record.setCategory(updatedRecord.getCategory());
        record.setDate(updatedRecord.getDate());

        return incomeRecordRepository.save(record);
    }

    // 🔹 Gelir kaydını sil
    public void deleteRecord(Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        IncomeRecord record = incomeRecordRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Record not found or access denied"));
        incomeRecordRepository.delete(record);
    }
}
