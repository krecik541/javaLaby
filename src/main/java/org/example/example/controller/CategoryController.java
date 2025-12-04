package org.example.example.controller;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.service.CategoryService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
public class CategoryController {

    @EJB
    private CategoryService categoryService;

    public CategoryController() {
    }


    public CategoryResponseDTO findById(UUID id) {
        return categoryService.findById(id)
                .map(category -> CategoryResponseDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .recipes(category.getRecipes().stream().map(Recipe::getId).toList())
                        .build())
                .orElse(null);
    }

    public List<CategoryResponseDTO> findAll() {
        System.out.println("CategoryController.findAll() called");
        List<CategoryResponseDTO> dtos = categoryService.findAll()
                .stream()
                .map(category -> CategoryResponseDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .recipes(category.getRecipes().stream().map(Recipe::getId).toList())
                        .build())
                .collect(Collectors.toList());
        System.out.println("CategoryController.findAll() returning " + dtos.size() + " DTOs");
        return dtos;
    }

    public UUID create(CategoryRequestDTO category) {
        return categoryService.create(category);
    }

    public UUID delete(UUID id) {

        UUID uuid = categoryService.delete(id);
//        if(uuid != null)
//            recipeController.deleteByCategory(uuid);
        return uuid;
    }

    public UUID update(UUID id, CategoryRequestDTO category) {
        return categoryService.update(id, category);
    }


}
