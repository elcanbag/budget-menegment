package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.ExpenseRecordDTO;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.model.ExpenseRecord;
import com.example.budgetmanager.model.ExpenseSubCategory;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.ExpenseRecordRepository;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.repository.ExpenseCategoryRepository;
import com.example.budgetmanager.repository.ExpenseSubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseRecordService {

    private final ExpenseRecordRepository expenseRecordRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseSubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;

    public List<ExpenseRecord> getRecordsByAuthenticatedUser(Authentication authentication) {
        return expenseRecordRepository.findByUserUsername(authentication.getName());
    }

    public ExpenseRecord getRecordByIdAndAuthenticatedUser(Long id, Authentication authentication) {
        return expenseRecordRepository.findByIdAndUserUsername(id, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Expense record not found"));
    }

    public void addExpenseRecord(ExpenseRecordDTO dto, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory category = categoryRepository.findById(dto.getCategoryId())
                .filter(cat -> cat.getUserId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to the user"));

        ExpenseSubCategory subCategory = null;
        if (dto.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(dto.getSubCategoryId())
                    .filter(subCat -> subCat.getParentCategory().getId().equals(category.getId()))
                    .filter(subCat -> subCat.getUser().getId().equals(user.getId()))
                    .orElseThrow(() -> new RuntimeException("Subcategory not found or does not belong to the user"));
        }

        ExpenseRecord record = ExpenseRecord.builder()
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .category(category)
                .subCategory(subCategory)
                .user(user)
                .date(LocalDateTime.now())
                .build();

        expenseRecordRepository.save(record);
    }

    public ExpenseRecord updateRecord(Long id, ExpenseRecord updatedRecord, Authentication authentication) {
        ExpenseRecord record = getRecordByIdAndAuthenticatedUser(id, authentication);
        record.setAmount(updatedRecord.getAmount());
        record.setDate(LocalDateTime.now());
        record.setCategory(updatedRecord.getCategory());
        return expenseRecordRepository.save(record);
    }

    public void deleteRecord(Long id, Authentication authentication) {
        ExpenseRecord record = getRecordByIdAndAuthenticatedUser(id, authentication);
        expenseRecordRepository.delete(record);
    }
}
