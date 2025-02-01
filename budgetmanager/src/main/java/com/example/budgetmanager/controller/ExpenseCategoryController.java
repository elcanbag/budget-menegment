package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ExpenseCategoryDTO;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.service.ExpenseCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseCategoryDTO>> getAllExpenseCategories() {
        return ResponseEntity.ok(expenseCategoryService.getAllExpenseCategories());
    }

    @PostMapping
    public ResponseEntity<String> addExpenseCategory(@RequestBody ExpenseCategoryDTO expenseCategoryDTO) {
        expenseCategoryService.addExpenseCategory(expenseCategoryDTO);
        return ResponseEntity.ok("Expense category added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateExpenseCategory(@PathVariable Long id, @RequestBody ExpenseCategoryDTO expenseCategoryDTO) {
        expenseCategoryService.updateExpenseCategory(id, expenseCategoryDTO);
        return ResponseEntity.ok("Expense category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpenseCategory(@PathVariable Long id) {
        expenseCategoryService.deleteExpenseCategory(id);
        return ResponseEntity.ok("Expense category deleted successfully");
    }
}
