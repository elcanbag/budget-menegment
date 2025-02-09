package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ExpenseSubCategoryDTO;
import com.example.budgetmanager.service.ExpenseSubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/expense-subcategories")
public class ExpenseSubCategoryController {

    private final ExpenseSubCategoryService subCategoryService;

    public ExpenseSubCategoryController(ExpenseSubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public ResponseEntity<String> addSubCategory(@RequestBody ExpenseSubCategoryDTO dto, Authentication authentication) {
        subCategoryService.addSubCategory(dto, authentication);
        return ResponseEntity.ok("Subcategory added successfully");
    }

    @GetMapping
    public ResponseEntity<List<ExpenseSubCategoryDTO>> getAllSubcategories(Authentication authentication) {
        return ResponseEntity.ok(subCategoryService.getAllSubcategories(authentication));
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<List<ExpenseSubCategoryDTO>> getSubcategories(@PathVariable Long parentId, Authentication authentication) {
        return ResponseEntity.ok(subCategoryService.getSubcategories(parentId, authentication));
    }
}
