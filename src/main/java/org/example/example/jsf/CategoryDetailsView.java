package org.example.example.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.CategoryController;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.CategoryResponseDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.service.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Inject
    private transient SecurityContext securityContext;

    @Inject
    private UserService userService;

    private String id;
    private CategoryResponseDTO category;
    private List<RecipeResponseDTO> recipes;
    private String recipeToDeleteId;
    
    // Pola filtrów
    private String titleFilter;
    private String descriptionFilter;
    private Integer preparationTimeFilter;
    private String authorNameFilter;
    
    // Lista użytkowników dla selecta
    private List<User> users;

    public void init() {
        if (id != null && !id.isBlank()) {
            UUID uuid = UUID.fromString(id);
            CategoryResponseDTO foundCategory = categoryController.findById(uuid);
            if (foundCategory != null) {
                this.category = foundCategory;
                // Load users for filter
                users = userService.findAll();
                // Load recipes for this category
                loadRecipes();
            }
        }
    }

    private void loadRecipes() {
        if (id == null || id.isBlank()) {
            recipes = List.of();
            return;
        }
        
        // Użycie filtrowania z Criteria API
        UUID categoryId = UUID.fromString(id);
        
        List<RecipeResponseDTO> allRecipes = recipeController.findByFilters(
                titleFilter,
                descriptionFilter,
                preparationTimeFilter,
                authorNameFilter,
                categoryId
        );
        
        // Check if user is admin
        SecurityContext sc = getSecurityContext();
        if (sc != null && sc.isCallerInRole(Role.ADMIN)) {
            recipes = allRecipes;
        } else if (sc != null && sc.getCallerPrincipal() != null) {
            // Filter to show only user's own recipes
            String login = sc.getCallerPrincipal().getName();
            Optional<User> userOpt = userService.findByLogin(login);
            
            if (userOpt.isPresent()) {
                UUID userId = userOpt.get().getId();
                recipes = allRecipes.stream()
                        .filter(recipe -> recipe.getAuthor().equals(userId))
                        .collect(Collectors.toList());
            } else {
                recipes = List.of();
            }
        } else {
            recipes = List.of();
        }
    }
    
    public void search() {
        loadRecipes();
    }
    
    public void clearFilters() {
        titleFilter = null;
        descriptionFilter = null;
        preparationTimeFilter = null;
        authorNameFilter = null;
        loadRecipes();
    }
    
    public String getUserName(UUID userId) {
        return userService.findById(userId)
                .map(User::getName)
                .orElse("Unknown");
    }

    public List<RecipeResponseDTO> getRecipes() {
        if (recipes == null) {
            loadRecipes();
        }
        return recipes;
    }

    public String deleteRecipe(String recipeToDelete) {
        if (recipeToDelete != null && !recipeToDelete.isBlank()) {
            try {
                recipeController.delete(UUID.fromString(recipeToDelete));
            } catch (Exception e) {
                System.err.println("Error deleting recipe: " + e.getMessage());
            }
        }
        // Reload recipes after deletion
        loadRecipes();
        return null;
    }

    public String deleteCategory(String id) {
        categoryController.delete(UUID.fromString(id));
        return "/categories/categories.xhtml?faces-redirect=true";
    }

    public boolean isAdmin() {
        SecurityContext sc = getSecurityContext();
        return sc != null && sc.isCallerInRole(Role.ADMIN);
    }

    private SecurityContext getSecurityContext() {
        if (securityContext != null) {
            return securityContext;
        }
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null) {
                // Try to reinject
                return securityContext;
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
