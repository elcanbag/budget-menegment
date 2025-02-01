package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.IncomeCategoryDTO;
import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.service.IncomeCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/income-categories")
public class IncomeCategoryController {

    private final IncomeCategoryService incomeCategoryService;

    public IncomeCategoryController(IncomeCategoryService incomeCategoryService) {
        this.incomeCategoryService = incomeCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<IncomeCategoryDTO>> getAllIncomeCategories() {
        return ResponseEntity.ok(incomeCategoryService.getAllIncomeCategories());
    }

    @PostMapping
    public ResponseEntity<String> addIncomeCategory(@RequestBody IncomeCategoryDTO incomeCategoryDTO) {
        incomeCategoryService.addIncomeCategory(incomeCategoryDTO);
        return ResponseEntity.ok("Income category added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIncomeCategory(@PathVariable Long id, @RequestBody IncomeCategoryDTO incomeCategoryDTO) {
        incomeCategoryService.updateIncomeCategory(id, incomeCategoryDTO);
        return ResponseEntity.ok("Income category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncomeCategory(@PathVariable Long id) {
        incomeCategoryService.deleteIncomeCategory(id);
        return ResponseEntity.ok("Income category deleted successfully");
    }
}
