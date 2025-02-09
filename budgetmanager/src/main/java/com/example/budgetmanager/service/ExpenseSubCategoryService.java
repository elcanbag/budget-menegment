package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.ExpenseSubCategoryDTO;
import com.example.budgetmanager.model.ExpenseSubCategory;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.ExpenseSubCategoryRepository;
import com.example.budgetmanager.repository.ExpenseCategoryRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseSubCategoryService {

    private final ExpenseSubCategoryRepository subCategoryRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseSubCategoryService(ExpenseSubCategoryRepository subCategoryRepository, ExpenseCategoryRepository categoryRepository, UserRepository userRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void addSubCategory(ExpenseSubCategoryDTO dto, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory parentCategory = categoryRepository.findById(dto.getParentCategoryId())
                .filter(category -> category.getUserId().equals(user.getId())) // Sadece kullanıcıya ait kategoriye izin ver
                .orElseThrow(() -> new RuntimeException("Parent category not found or does not belong to the user"));

        ExpenseSubCategory subCategory = new ExpenseSubCategory();
        subCategory.setName(dto.getName());
        subCategory.setParentCategory(parentCategory);
        subCategory.setUser(user);

        subCategoryRepository.save(subCategory);
    }


    public List<ExpenseSubCategoryDTO> getAllSubcategories(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subCategoryRepository.findByUser(user)
                .stream()
                .map(subCategory -> new ExpenseSubCategoryDTO(subCategory.getId(), subCategory.getName(), subCategory.getParentCategory().getId()))
                .collect(Collectors.toList());
    }

    public List<ExpenseSubCategoryDTO> getSubcategories(Long parentId, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));

        return subCategoryRepository.findByParentCategoryAndUser(parentCategory, user)
                .stream()
                .map(subCategory -> new ExpenseSubCategoryDTO(subCategory.getId(), subCategory.getName(), parentId))
                .collect(Collectors.toList());
    }
}
