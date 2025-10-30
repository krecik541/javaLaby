package org.example.example.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("categoryDetailsView")
@ViewScoped
@Getter
@Setter
public class CategoryDetailsView implements Serializable {

    @Inject
    private CategoryController categoryController;

    @Inject
    private RecipeController recipeController;

    private String id;
    private CategoryResponseDTO category;
    private List<RecipeResponseDTO> recipes;

    public void init() {
        if (id != null && !id.isBlank()) {
            try {
                UUID uuid = UUID.fromString(id);
                CategoryResponseDTO foundCategory = categoryController.findById(uuid);
                if (foundCategory != null) {
                    this.category = foundCategory;
                } else {
                    // Redirect to categories list if category not found
                    try {
                        FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .redirect("/categories/categories.xhtml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalArgumentException e) {
                // Handle invalid UUID format
                try {
                    FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("/categories/categories.xhtml");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<RecipeResponseDTO> getRecipes() {
        if (id == null || id.isBlank()) {
            return List.of();
        }
        return recipeController.findByCategory(id);
    }

    public void deleteRecipe(String recipeToDelete) {
        recipeController.delete(UUID.fromString(recipeToDelete));
    }

    public void deleteCategory(String id) {
        categoryController.delete(UUID.fromString(id));
    }
}
