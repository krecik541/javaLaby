package org.example.example.jsf;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.dtos.CategoryResponseDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Named("categoryView")
@ViewScoped
@Getter
@Setter
public class CategoryView implements Serializable {

    private CategoryController categoryController;
    private RecipeController recipeController;
    private List<CategoryResponseDTO> categories;

    @Inject
    private transient SecurityContext securityContext;

    public CategoryView() {
        System.out.println("CategoryView constructor called");
    }

    @Inject
    public CategoryView(CategoryController categoryController, RecipeController recipeController) {
        this.categoryController = categoryController;
        this.recipeController = recipeController;
        categories = categoryController.findAll();
        System.out.println("CategoryView constructor called");
    }

    public List<CategoryResponseDTO> getCategories() {
        categories = categoryController.findAll();
        return categories;
    }

    public String delete(String uuid) {
        try {
            UUID id = UUID.fromString(uuid);
            categoryController.findById(id);
            categoryController.delete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "/categories/categories.xhtml?faces-redirect=true";
    }

    public boolean isAdmin() {
        return securityContext != null && securityContext.isCallerInRole(Role.ADMIN);
    }
}
