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
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.service.CategoryService;
import org.example.example.service.UserService;

import java.io.Serializable;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Named("recipeFormView")
@ViewScoped
@Getter
@Setter
public class RecipeFormView implements Serializable {
    private CategoryController categoryController;
    private RecipeController recipeController;
    private CategoryService categoryService;

    @Inject
    private transient SecurityContext securityContext;

    @Inject
    private UserService userService;

    public RecipeFormView() {}

    private String id;
    private String categoryId;
    private Recipe recipe = new Recipe();
    private boolean accessDenied = false;

    @Inject
    public RecipeFormView(CategoryController categoryController, RecipeController recipeController, CategoryService categoryService) {
        this.categoryController = categoryController;
        this.recipeController = recipeController;
        this.categoryService = categoryService;
    }

    public void init() {
        if (id != null && !id.isBlank()) {
            try {
                UUID uuid = UUID.fromString(id);
                Recipe res = recipeController.findById(uuid);
                if (res != null) {
                    // Check access permissions - only owner or admin can edit
                    SecurityContext sc = getSecurityContext();
                    if (sc != null && sc.getCallerPrincipal() != null) {
                        String login = sc.getCallerPrincipal().getName();
                        
                        // Admin can edit all
                        if (sc.isCallerInRole(Role.ADMIN)) {
                            recipe.setId(res.getId());
                            recipe.setTitle(res.getTitle());
                            recipe.setDescription(res.getDescription());
                            recipe.setPreparationTime(res.getPreparationTime());
                            recipe.setCategory(res.getCategory());
                            recipe.setDateOfAddition(res.getDateOfAddition());
                        } else {
                            // Regular user can only edit their own recipes
                            Optional<User> userOpt = userService.findByLogin(login);
                            if (userOpt.isPresent() && userOpt.get().getId().equals(res.getAuthor().getId())) {
                                recipe.setId(res.getId());
                                recipe.setTitle(res.getTitle());
                                recipe.setDescription(res.getDescription());
                                recipe.setPreparationTime(res.getPreparationTime());
                                recipe.setCategory(res.getCategory());
                                recipe.setDateOfAddition(res.getDateOfAddition());
                            } else {
                                accessDenied = true;
                            }
                        }
                    } else {
                        accessDenied = true;
                    }
                }
            } catch (IllegalArgumentException ignored) {}
        } else if (categoryId != null && !categoryId.isBlank()) {
            try {
                UUID cid = UUID.fromString(categoryId);
                recipe.setCategory(categoryService.findById(cid).get());
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
                        .category(recipe.getCategory().getId())
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
                        .category(recipe.getCategory().getId())
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

    public String delete() {
        try {
            if (recipe != null && recipe.getId() != null) {
                recipeController.delete(recipe.getId());
                return "/categories/categories.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
