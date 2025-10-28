package org.example.example.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.repository.RecipeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class RecipeService {

    private RecipeRepository recipeRepository;
    private UserService userService;
    private CategoryService categoryService;

    public RecipeService() {
    }

    @Inject
    public RecipeService(RecipeRepository recipeRepository, UserService userService, CategoryService categoryService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public Optional<Recipe> findById(UUID id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public UUID create(RecipeRequestDTO dto) {
        Recipe recipe = Recipe.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .preparationTime(dto.getPreparationTime())
                .dateOfAddition(java.sql.Date.valueOf(LocalDate.now()))

                .author(dto.getAuthor())
                .category(dto.getCategory())
                .build();
        recipeRepository.create(recipe);

        if (recipe.getCategory() != null) {
            Category category = categoryService.findById(recipe.getCategory()).orElse(null);
            if (category != null) {
                category.getRecipes().add(recipe.getId());
            }
        }

        if (recipe.getAuthor() != null) {
            User author = userService.findById(recipe.getAuthor()).orElse(null);
            if (author != null) {
                author.getRecipes().add(recipe.getId());
            }
        }

        return recipe.getId();
    }

    public UUID delete(UUID id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        if (recipe.getCategory() != null) {
            Category category = categoryService.findById(recipe.getCategory()).orElse(null);
            if (category != null) {
                category.getRecipes().remove(recipe.getId());
            }
        }

        if (recipe.getAuthor() != null) {
            User author = userService.findById(recipe.getAuthor()).orElse(null);
            if (author != null) {
                author.getRecipes().remove(recipe.getId());
            }
        }

        recipeRepository.delete(id);
        return id;
    }

    public UUID update(UUID uuid, RecipeRequestDTO dto) {
        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(uuid);
        if (existingRecipeOpt.isEmpty()) {
            throw new IllegalArgumentException("Recipe not found");
        }

        Recipe existingRecipe = existingRecipeOpt.get();
        Recipe user = Recipe.builder()
                .id(uuid)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .preparationTime(dto.getPreparationTime())
                .dateOfAddition(java.sql.Date.valueOf(LocalDate.now()))
                .author(dto.getAuthor())
                .category(dto.getCategory())
                .build();
        recipeRepository.update(uuid, user);
        return uuid;
    }
}
