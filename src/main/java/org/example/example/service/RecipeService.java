package org.example.example.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.persistance.repository.RecipeRepository;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@LocalBean
@Stateless
public class RecipeService {

    private RecipeRepository recipeRepository;
    @EJB
    private UserService userService;
    @EJB
    private CategoryService categoryService;

    @Inject
    private SecurityContext securityContext;

    public RecipeService() {
    }

    @Inject
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> findById(UUID id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public List<RecipeResponseDTO> findAllDtos(UUID category) {
        if(securityContext != null && securityContext.isCallerInRole(Role.ADMIN))
            return findAll()
                    .stream()
                    .map(recipe -> RecipeResponseDTO.builder()
                            .id(recipe.getId())
                            .author(recipe.getAuthor().getId())
                            .category(recipe.getCategory().getId())
                            .dateOfAddition(recipe.getDateOfAddition())
                            .description(recipe.getDescription())
                            .preparationTime(recipe.getPreparationTime())
                            .title(recipe.getTitle())
                            .build())
                    .collect(Collectors.toList());
        else {
            Principal principal = securityContext.getCallerPrincipal();
            if (principal != null) {
                Optional<User> user = userService.findByLogin(principal.getName());
                return recipeRepository.findByAuthorAndCategory(user.get().getId(), category)
                        .stream()
                        .map(recipe -> RecipeResponseDTO.builder()
                                .id(recipe.getId())
                                .author(recipe.getAuthor().getId())
                                .category(recipe.getCategory().getId())
                                .dateOfAddition(recipe.getDateOfAddition())
                                .description(recipe.getDescription())
                                .preparationTime(recipe.getPreparationTime())
                                .title(recipe.getTitle())
                                .build())
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        }
    }

    @RolesAllowed({Role.ADMIN, Role.USER})
    public List<Recipe> findForCurrentUser() {
        if (securityContext != null && securityContext.isCallerInRole(Role.ADMIN)) {
            return recipeRepository.findAll();
        }
        Principal principal = securityContext == null ? null : securityContext.getCallerPrincipal();
        if (principal == null) return List.of();
        Optional<User> userOpt = userService.findByLogin(principal.getName());
        if (userOpt.isEmpty()) return List.of();
        return recipeRepository.findByAuthor(userOpt.get().getId());
    }

    @RolesAllowed({Role.ADMIN, Role.USER})
    public UUID create(RecipeRequestDTO dto) {
        Recipe recipe = Recipe.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .preparationTime(dto.getPreparationTime())
                .dateOfAddition(Date.valueOf(LocalDate.now()))

//                .author(userService.findById(dto.getAuthor()).orElse(null))
                .category(categoryService.findById(dto.getCategory()).get())
                .build();

        if(securityContext != null && securityContext.getCallerPrincipal() != null) {
            String s = securityContext.getCallerPrincipal().getName();
            userService.findByLogin(s).ifPresent(recipe::setAuthor);
        }

        recipeRepository.create(recipe);

        if (recipe.getCategory() != null) {
            Category category = categoryService.findById(recipe.getCategory().getId()).orElse(null);
            if (category != null) {
                category.getRecipes().add(recipeRepository.findById(recipe.getId()).get());
            }
        }

        if (recipe.getAuthor() != null) {
            User author = userService.findById(recipe.getAuthor().getId()).orElse(null);
            if (author != null) {
                author.getRecipes().add(recipe);
            }
        }

        return recipe.getId();
    }

    public UUID delete(UUID id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        if (recipe.getCategory() != null) {
            Category category = categoryService.findById(recipe.getCategory().getId()).orElse(null);
            if (category != null) {
                category.getRecipes().remove(recipe.getId());
            }
        }

        if (recipe.getAuthor() != null) {
            User author = userService.findById(recipe.getAuthor().getId()).orElse(null);
            if (author != null) {
                author.getRecipes().remove(recipe.getId());
            }
        }

        recipeRepository.delete(id);
        return id;
    }

    public UUID update(UUID uuid, RecipeRequestDTO dto) {
        User user = null;
        if(securityContext != null && securityContext.getCallerPrincipal() != null) {
            String s = securityContext.getCallerPrincipal().getName();
            user = userService.findByLogin(s).get();
        }

        if(user == null)
            return null;
        List<Recipe> r = recipeRepository.findByAuthor(user.getId());

        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(uuid);
        if (existingRecipeOpt.isEmpty()) {
            throw new IllegalArgumentException("Recipe not found");
        }
        if (!r.contains(existingRecipeOpt.get()) && !user.getRoles().contains(Role.ADMIN))
            throw new IllegalArgumentException("Recipe not found");

        Recipe existingRecipe = existingRecipeOpt.get();
        Recipe recipe = Recipe.builder()
                .id(uuid)
                .title(dto.getTitle() != null && !dto.getTitle().isEmpty() ? dto.getTitle() : existingRecipe.getTitle())
                .description(dto.getDescription()  != null && !dto.getDescription().isEmpty() ? dto.getDescription() : existingRecipe.getDescription())
                .preparationTime(dto.getPreparationTime())
                .dateOfAddition(Date.valueOf(LocalDate.now()))
                .author(existingRecipe.getAuthor())
                .category(existingRecipe.getCategory())
                .build();
        recipeRepository.update(uuid, recipe);
        return uuid;
    }

    public void deleteByCategory(UUID id) {
        User user = null;
        if(securityContext != null && securityContext.getCallerPrincipal() != null) {
            String s = securityContext.getCallerPrincipal().getName();
            user = userService.findByLogin(s).get();
        }

        if(user == null)
            return;
        List<Recipe> r = recipeRepository.findByAuthor(user.getId());

        Optional<Recipe> existingRecipeOpt = recipeRepository.findById(id);
        if (existingRecipeOpt.isEmpty()) {
            throw new IllegalArgumentException("Recipe not found");
        }
        if (!r.contains(existingRecipeOpt.get()) && !user.getRoles().contains(Role.ADMIN))
            throw new IllegalArgumentException("Recipe not found");
        recipeRepository.deleteByCategory(id);
    }

    public List<Recipe> findByAuthor(UUID id) {
        return recipeRepository.findByAuthor(id);
    }
}
