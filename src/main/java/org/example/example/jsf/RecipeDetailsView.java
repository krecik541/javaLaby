package org.example.example.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.service.UserService;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Named("recipeDetailsView")
@ViewScoped
@Getter
@Setter
public class RecipeDetailsView implements Serializable {

    private RecipeController recipeController;
    private CategoryController categoryController;

    @Inject
    private transient SecurityContext securityContext;

    @Inject
    private UserService userService;

    private String id;
    private RecipeResponseDTO recipe;
    private CategoryResponseDTO category;
    private boolean accessDenied = false;

    public RecipeDetailsView() {
    }

    @Inject
    public RecipeDetailsView(RecipeController recipeController, CategoryController categoryController) {
        this.recipeController = recipeController;
        this.categoryController = categoryController;
    }

    public void init() {
        try {
            if (id == null || id.isBlank()) {
                accessDenied = true;
                return;
            }
            
            Recipe r = recipeController.findById(UUID.fromString(id));
            
            // Check access permissions
            if (r != null) {
                // Check if user is admin or owner
                SecurityContext sc = getSecurityContext();
                if (sc != null && sc.getCallerPrincipal() != null) {
                    String login = sc.getCallerPrincipal().getName();
                    
                    // Admin can view all
                    if (sc.isCallerInRole(Role.ADMIN)) {
                        recipe = RecipeResponseDTO.builder()
                                .id(r.getId())
                                .title(r.getTitle())
                                .description(r.getDescription())
                                .preparationTime(r.getPreparationTime())
                                .dateOfAddition(r.getDateOfAddition())
                                .author(r.getAuthor().getId())
                                .category(r.getCategory().getId())
                                .build();
                    } else {
                        // Regular user can only view their own recipes
                        Optional<User> userOpt = userService.findByLogin(login);
                        if (userOpt.isPresent() && userOpt.get().getId().equals(r.getAuthor().getId())) {
                            recipe = RecipeResponseDTO.builder()
                                    .id(r.getId())
                                    .title(r.getTitle())
                                    .description(r.getDescription())
                                    .preparationTime(r.getPreparationTime())
                                    .dateOfAddition(r.getDateOfAddition())
                                    .author(r.getAuthor().getId())
                                    .category(r.getCategory().getId())
                                    .build();
                        } else {
                            accessDenied = true;
                        }
                    }
                } else {
                    accessDenied = true;
                }
            } else {
                accessDenied = true;
            }
        } catch (Exception ignored) {
            accessDenied = true;
        }
    }

    public String getCategoryName(String categoryId) {
        CategoryResponseDTO c = categoryController.findById(UUID.fromString(categoryId));
        return c != null ? c.getName() : "-";
    }

    private SecurityContext getSecurityContext() {
        if (securityContext != null) {
            return securityContext;
        }
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null) {
                return securityContext;
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
