package com.example.budgetmanager.controller;

import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.service.IncomeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/income-records") // 🔄 Değiştirildi
public class IncomeRecordController {

    private final IncomeRecordService incomeRecordService;

    public IncomeRecordController(IncomeRecordService incomeRecordService) {
        this.incomeRecordService = incomeRecordService;
    }

    // 🔹 Tüm gelir kayıtlarını getir
    @GetMapping
    public ResponseEntity<List<IncomeRecord>> getUserIncomeRecords() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.getRecordsByAuthenticatedUser(authentication));
    }

    // 🔹 Belirli bir gelir kaydını getir
    @GetMapping("/{id}")
    public ResponseEntity<IncomeRecord> getIncomeRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.getRecordByIdAndAuthenticatedUser(id, authentication));
    }

    // 🔹 Yeni gelir kaydı ekle
    @PostMapping
    public ResponseEntity<IncomeRecord> addIncomeRecord(@RequestBody IncomeRecord incomeRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.addRecord(incomeRecord, authentication));
    }

    // 🔹 Gelir kaydını güncelle
    @PutMapping("/{id}")
    public ResponseEntity<IncomeRecord> updateIncomeRecord(@PathVariable Long id, @RequestBody IncomeRecord updatedRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.updateRecord(id, updatedRecord, authentication));
    }

    // 🔹 Gelir kaydını sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        incomeRecordService.deleteRecord(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
