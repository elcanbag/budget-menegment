package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.ExpenseCategoryDTO;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.repository.ExpenseCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    public List<ExpenseCategoryDTO> getAllExpenseCategories() {
        return expenseCategoryRepository.findAll()
                .stream()
                .map(entity -> new ExpenseCategoryDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    public void addExpenseCategory(ExpenseCategoryDTO expenseCategoryDTO) {
        ExpenseCategory entity = new ExpenseCategory();
        entity.setName(expenseCategoryDTO.getName());
        expenseCategoryRepository.save(entity);
    }

    public void updateExpenseCategory(Long id, ExpenseCategoryDTO expenseCategoryDTO) {
        ExpenseCategory entity = expenseCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        entity.setName(expenseCategoryDTO.getName());
        expenseCategoryRepository.save(entity);
    }

    public void deleteExpenseCategory(Long id) {
        expenseCategoryRepository.deleteById(id);
    }
}
