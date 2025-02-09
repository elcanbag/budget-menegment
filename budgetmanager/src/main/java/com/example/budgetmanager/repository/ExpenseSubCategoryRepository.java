package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.ExpenseSubCategory;
import com.example.budgetmanager.model.ExpenseCategory;
import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseSubCategoryRepository extends JpaRepository<ExpenseSubCategory, Long> {
    List<ExpenseSubCategory> findByParentCategoryAndUser(ExpenseCategory parentCategory, User user);
    List<ExpenseSubCategory> findByUser(User user);
}
