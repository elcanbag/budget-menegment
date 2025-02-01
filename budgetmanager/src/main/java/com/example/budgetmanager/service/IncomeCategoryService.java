package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.IncomeCategoryDTO;
import com.example.budgetmanager.model.IncomeCategory;
import com.example.budgetmanager.repository.IncomeCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncomeCategoryService {

    private final IncomeCategoryRepository incomeCategoryRepository;

    public IncomeCategoryService(IncomeCategoryRepository incomeCategoryRepository) {
        this.incomeCategoryRepository = incomeCategoryRepository;
    }

    public List<IncomeCategoryDTO> getAllIncomeCategories() {
        return incomeCategoryRepository.findAll()
                .stream()
                .map(entity -> new IncomeCategoryDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    public void addIncomeCategory(IncomeCategoryDTO incomeCategoryDTO) {
        IncomeCategory entity = new IncomeCategory();
        entity.setName(incomeCategoryDTO.getName());
        incomeCategoryRepository.save(entity);
    }

    public void updateIncomeCategory(Long id, IncomeCategoryDTO incomeCategoryDTO) {
        IncomeCategory entity = incomeCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        entity.setName(incomeCategoryDTO.getName());
        incomeCategoryRepository.save(entity);
    }

    public void deleteIncomeCategory(Long id) {
        incomeCategoryRepository.deleteById(id);
    }
}
