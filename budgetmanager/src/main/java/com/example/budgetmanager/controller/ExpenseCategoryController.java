package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ExpenseCategoryDTO;
import com.example.budgetmanager.service.ExpenseCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense-categories")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseCategoryDTO>> getAllExpenseCategories(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(expenseCategoryService.getAllExpenseCategories(username));
    }

    @PostMapping
    public ResponseEntity<String> addExpenseCategory(@RequestBody ExpenseCategoryDTO expenseCategoryDTO, Authentication authentication) {
        String username = authentication.getName();
        expenseCategoryService.addExpenseCategory(expenseCategoryDTO, username);
        return ResponseEntity.ok("Expense category added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateExpenseCategory(@PathVariable Long id, @RequestBody ExpenseCategoryDTO expenseCategoryDTO, Authentication authentication) {
        String username = authentication.getName();
        expenseCategoryService.updateExpenseCategory(id, expenseCategoryDTO, username);
        return ResponseEntity.ok("Expense category updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpenseCategory(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        expenseCategoryService.deleteExpenseCategory(id, username);
        return ResponseEntity.ok("Expense category deleted successfully");
    }


    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<ExpenseCategoryDTO>> getSubcategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(expenseCategoryService.getSubcategories(parentId));
    }

}
