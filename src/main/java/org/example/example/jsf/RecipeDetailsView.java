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
import org.example.example.persistance.dtos.RecipeResponseDTO;

import java.io.Serializable;
import java.util.UUID;

@Named("recipeDetailsView")
@ViewScoped
@Getter
@Setter
public class RecipeDetailsView implements Serializable {

    private RecipeController recipeController;
    private CategoryController categoryController;

    private String id;
    private RecipeResponseDTO recipe;
    private CategoryResponseDTO category;

    public RecipeDetailsView() {}

    @Inject
    public RecipeDetailsView(RecipeController recipeController, CategoryController categoryController) {
        this.recipeController = recipeController;
        this.categoryController = categoryController;
    }

    public void init() {
        try {
            Recipe r = recipeController.findById(UUID.fromString(id));
            recipe = RecipeResponseDTO.builder()
                    .id(r.getId())
                    .title(r.getTitle())
                    .description(r.getDescription())
                    .preparationTime(r.getPreparationTime())
                    .dateOfAddition(r.getDateOfAddition())
                    .author(r.getAuthor())
                    .category(r.getCategory())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getCategoryName(String categoryId) {
        CategoryResponseDTO c = categoryController.findById(UUID.fromString(categoryId));
        return c != null ? c.getName() : "-";
    }
}
