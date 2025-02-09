package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.ExpenseCategoryDTO;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.ExpenseCategoryRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserRepository userRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository, UserRepository userRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userRepository = userRepository;
    }

    public List<ExpenseCategoryDTO> getAllExpenseCategories(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return expenseCategoryRepository.findByUserId(user.getId())
                .stream()
                .map(entity -> new ExpenseCategoryDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    public void addExpenseCategory(ExpenseCategoryDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory category = new ExpenseCategory();
        category.setName(dto.getName());
        category.setUserId(user.getId());

        if (dto.getParentId() != null) {
            ExpenseCategory parentCategory = expenseCategoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        expenseCategoryRepository.save(category);
    }

    public List<ExpenseCategoryDTO> getSubcategories(Long parentId) {
        ExpenseCategory parentCategory = expenseCategoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));

        return expenseCategoryRepository.findByParentCategory(parentCategory)
                .stream()
                .map(category -> new ExpenseCategoryDTO(category.getId(), category.getName(), parentId))
                .collect(Collectors.toList());
    }


    public void updateExpenseCategory(Long id, ExpenseCategoryDTO expenseCategoryDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory entity = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!entity.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to modify this category");
        }

        entity.setName(expenseCategoryDTO.getName());
        expenseCategoryRepository.save(entity);
    }

    public void deleteExpenseCategory(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory entity = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!entity.getUserId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this category");
        }

        expenseCategoryRepository.deleteById(id);
    }
}
