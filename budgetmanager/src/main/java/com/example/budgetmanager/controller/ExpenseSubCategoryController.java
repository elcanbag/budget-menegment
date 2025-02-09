package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.ExpenseSubCategoryDTO;
import com.example.budgetmanager.service.ExpenseSubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense-subcategories")
@RequiredArgsConstructor
public class ExpenseSubCategoryController {

    private final ExpenseSubCategoryService subCategoryService;

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

    @PutMapping("/{subCategoryId}")
    public ResponseEntity<String> updateSubCategory(@PathVariable Long subCategoryId,
                                                    @RequestBody ExpenseSubCategoryDTO dto,
                                                    Authentication authentication) {
        subCategoryService.updateSubCategory(subCategoryId, dto, authentication);
        return ResponseEntity.ok("Subcategory updated successfully");
    }


    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId, Authentication authentication) {
        subCategoryService.deleteSubCategory(subCategoryId, authentication);
        return ResponseEntity.ok("Subcategory deleted successfully");
    }
}
