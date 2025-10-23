package org.example.example.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService() {
    }

    @Inject
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public UUID create(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .recipes(new ArrayList<>())
                .build();
        validate(categoryRepository.create(category));
        return category.getId();
    }

    public UUID delete(UUID id) {
        validate(categoryRepository.delete(id));
        return id;
    }

    public UUID update(UUID uuid, CategoryRequestDTO dto) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(uuid);
        if (existingCategoryOpt.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }

        Category existingCategory = existingCategoryOpt.get();
        Category category = Category.builder()
                .id(uuid)
                .name(dto.getName())
                .type(dto.getType())
                .recipes(existingCategory.getRecipes())
                .build();
        validate(categoryRepository.update(uuid, category));
        return uuid;
    }

    private void validate(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Operation failed");
        }
    }
}
