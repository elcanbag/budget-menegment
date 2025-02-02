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

    public void addExpenseCategory(ExpenseCategoryDTO expenseCategoryDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory entity = new ExpenseCategory();
        entity.setName(expenseCategoryDTO.getName());
        entity.setUserId(user.getId());
        expenseCategoryRepository.save(entity);
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
