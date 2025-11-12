package org.example.example.service;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CategoryService {

    private CategoryRepository categoryRepository;
    private RecipeService recipeService;

    public CategoryService() {
    }

    @Inject
    public CategoryService(CategoryRepository categoryRepository, RecipeService recipeService) {
        this.categoryRepository = categoryRepository;
        this.recipeService = recipeService;
    }

    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();
        System.out.println("CategoryService.findAll() found " + categories.size() + " categories");
        return categories;
    }

    @Transactional
    public UUID create(CategoryRequestDTO dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .recipes(new ArrayList<>())
                .build();
        validate(categoryRepository.create(category));
        return category.getId();
    }

    @Transactional
    public UUID delete(UUID id) {
        Optional<Category> categoryOpt = findById(id);
        if (categoryOpt.isPresent()) {
            // delete all recipes that belong to this category
            Category category = categoryOpt.get();
            if (category.getRecipes() != null) {
                // iterate over a copy to avoid ConcurrentModification
                List<UUID> toDelete = category.getRecipes().stream()
                        .map(Recipe::getId)
                        .toList();
                for (UUID recipeId : toDelete) {
                    // delegate deletion to RecipeService to handle updates to authors/categories
                    try {
                        recipeService.delete(recipeId);
                    } catch (Exception e) {
                        // swallow exceptions per-delete to attempt best-effort cleanup
                    }
                }
            }

            categoryRepository.delete(id);
            return id;
        }
        return null;
    }


    @Transactional
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

    public int countRecipesByCategory(UUID id) {
        Optional<Category> cc = findById(id);
        Category c = cc.get();
        return c != null && c.getRecipes() != null ? c.getRecipes().size() : 0;
    }

    private void validate(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Operation failed");
        }
    }
}
