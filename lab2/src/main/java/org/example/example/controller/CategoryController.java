package org.example.example.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.dtos.UserResponseDTO;
import org.example.example.service.CategoryService;
import org.example.example.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController() {
    }

    @Inject
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryResponseDTO findById(UUID id) {
        return categoryService.findById(id)
                .map(category -> CategoryResponseDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .recipes(category.getRecipes())
                        .build())
                .orElseThrow();
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll()
                .stream()
                .map(category -> CategoryResponseDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .recipes(category.getRecipes())
                        .build())
                .collect(Collectors.toList());
    }

    public UUID create(CategoryRequestDTO category) {
        return categoryService.create(category);
    }

    public UUID delete(UUID id) {
        return categoryService.delete(id);
    }

    public UUID update(UUID id, CategoryRequestDTO category) {
        return categoryService.update(id, category);
    }
}
