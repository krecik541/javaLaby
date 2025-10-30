package org.example.example.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Named("recipeFormView")
@ViewScoped
@Getter
@Setter
public class RecipeFormView implements Serializable {
    private CategoryController categoryController;
    private RecipeController recipeController;

    public RecipeFormView() {}

    private String id;
    private String categoryId;
    private Recipe recipe = new Recipe();

    @Inject
    public RecipeFormView(CategoryController categoryController, RecipeController recipeController) {
        this.categoryController = categoryController;
        this.recipeController = recipeController;
    }

    public void init() {
        if (id != null && !id.isBlank()) {
            try {
                UUID uuid = UUID.fromString(id);
                Recipe res = recipeController.findById(uuid);
                if (res != null) {
                    recipe.setId(res.getId());
                    recipe.setTitle(res.getTitle());
                    recipe.setDescription(res.getDescription());
                    recipe.setPreparationTime(res.getPreparationTime());
                    recipe.setCategory(res.getCategory());
                    recipe.setDateOfAddition(res.getDateOfAddition());
                }
            } catch (IllegalArgumentException ignored) {}
        } else if (categoryId != null && !categoryId.isBlank()) {
            try {
                UUID cid = UUID.fromString(categoryId);
                recipe.setCategory(cid);
            } catch (IllegalArgumentException ignored) {}
        }
        if (recipe.getDateOfAddition() == null) {
            recipe.setDateOfAddition(java.sql.Date.valueOf(LocalDate.now()));
        }
    }

    public List<CategoryResponseDTO> getCategories() {
        return categoryController.findAll();
    }

    public String save() {
        try {
            if (recipe.getId() == null) {
                RecipeRequestDTO req = RecipeRequestDTO.builder()
                        .title(recipe.getTitle())
                        .description(recipe.getDescription())
                        .preparationTime(recipe.getPreparationTime())
                        .category(recipe.getCategory())
                        .build();
                UUID uuid = recipeController.create(req);
                if (uuid != null) {
                    return "/recipes/recipe-detail.xhtml?id=" + uuid + "&faces-redirect=true";
                }
            } else {
                UUID rid = recipe.getId();
                RecipeRequestDTO req = RecipeRequestDTO.builder()
                        .title(recipe.getTitle())
                        .description(recipe.getDescription())
                        .preparationTime(recipe.getPreparationTime())
                        .category(recipe.getCategory())
                        .build();
                UUID uuid = recipeController.update(rid, req);
                if (uuid != null) {
                    return "/recipes/recipe-detail.xhtml?id=" + uuid + "&faces-redirect=true";
                }
            }
        } catch (Exception e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
        }
        return null;
    }
}
