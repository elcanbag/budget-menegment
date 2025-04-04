package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.IncomeRecordDTO;
import com.example.budgetmanager.model.IncomeRecord;
import com.example.budgetmanager.service.IncomeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/income-records")
public class IncomeRecordController {

    private final IncomeRecordService incomeRecordService;

    public IncomeRecordController(IncomeRecordService incomeRecordService) {
        this.incomeRecordService = incomeRecordService;
    }


    @GetMapping
    public ResponseEntity<List<IncomeRecord>> getUserIncomeRecords() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.getRecordsByAuthenticatedUser(authentication));
    }


    @GetMapping("/{id}")
    public ResponseEntity<IncomeRecord> getIncomeRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.getRecordByIdAndAuthenticatedUser(id, authentication));
    }


    @PostMapping
    public ResponseEntity<IncomeRecord> addIncomeRecord(@RequestBody IncomeRecordDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.addRecord(dto, authentication));
    }



    @PutMapping("/{id}")
    public ResponseEntity<IncomeRecord> updateIncomeRecord(@PathVariable Long id, @RequestBody IncomeRecord updatedRecord) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(incomeRecordService.updateRecord(id, updatedRecord, authentication));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeRecord(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        incomeRecordService.deleteRecord(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<IncomeRecord>> filterIncomeRecords(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Authentication authentication) {
        return ResponseEntity.ok(incomeRecordService.getRecordsByDateRange(authentication, start, end));
    }

}
