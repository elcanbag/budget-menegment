package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.IncomeCategoryDTO;
import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.IncomeCategoryRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;
    private final UserRepository userRepository;

    public IncomeCategoryService(IncomeCategoryRepository incomeCategoryRepository, UserRepository userRepository) {
        this.incomeCategoryRepository = incomeCategoryRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<IncomeCategoryDTO> getAllIncomeCategoriesForUser() {
        User user = getAuthenticatedUser();
        return incomeCategoryRepository.findByUser(user)
                .stream()
                .map(category -> new IncomeCategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public void addIncomeCategory(IncomeCategoryDTO incomeCategoryDTO) {
        User user = getAuthenticatedUser();
        IncomeCategory category = new IncomeCategory();
        category.setName(incomeCategoryDTO.getName());
        category.setUser(user);
        incomeCategoryRepository.save(category);
    }

    public void updateIncomeCategory(Long id, IncomeCategoryDTO incomeCategoryDTO) {
        User user = getAuthenticatedUser();
        IncomeCategory category = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this category");
        }

        category.setName(incomeCategoryDTO.getName());
        incomeCategoryRepository.save(category);
    }

    public void deleteIncomeCategory(Long id) {
        User user = getAuthenticatedUser();
        IncomeCategory category = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this category");
        }

        incomeCategoryRepository.deleteById(id);
    }
}
