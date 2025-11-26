package org.example.example.controller;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.service.RecipeService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeController {

    @EJB
    private RecipeService recipeService;

    @Inject
    private SecurityContext securityContext;

//    public RecipeController(SecurityContext securityContext) {
//        this.securityContext = securityContext;
//    }


    public Recipe findById(UUID id) {
        return recipeService.findById(id).orElse(null);
//                .findById(id)
//                .map(recipe -> RecipeResponseDTO.builder()
//                        .id(recipe.getId())
//                        .author(recipe.getAuthor())
//                        .category(recipe.getCategory())
//                        .dateOfAddition(recipe.getDateOfAddition())
//                        .description(recipe.getDescription())
//                        .preparationTime(recipe.getPreparationTime())
//                        .title(recipe.getTitle())
//                        .build())
//                .orElseThrow();
    }

    public List<RecipeResponseDTO> findAllDtos(UUID category) {
        return recipeService.findAllDtos(category);
    }

    public UUID create(RecipeRequestDTO recipe) {
        return recipeService.create(recipe);
    }

    public UUID delete(UUID id) {
        return recipeService.delete(id);
    }

    public UUID update(UUID id, RecipeRequestDTO recipe) {
        return recipeService.update(id, recipe);
    }

    public void deleteByCategory(UUID id) {
        recipeService.deleteByCategory(id);
    }

    public List<RecipeResponseDTO> findByCategory(String id) {
        List<Recipe> rr = recipeService.findAll();
        rr = rr.stream().filter(recipe -> recipe.getCategory().getId().toString().equals(id)).toList();

        List<RecipeResponseDTO> r = recipeService.findAll().stream()
                .filter(recipe -> recipe.getCategory().getId().toString().equals(id))
                .map(recipe -> RecipeResponseDTO.builder()
                .id(recipe.getId())
                .author(recipe.getAuthor().getId())
                .category(recipe.getCategory().getId())
                .dateOfAddition(recipe.getDateOfAddition())
                .description(recipe.getDescription())
                .preparationTime(recipe.getPreparationTime())
                .title(recipe.getTitle())
                .build()).toList();
        return r;
    }

    public List<RecipeResponseDTO> findAll() {
        return recipeService.findAll().stream()
                .map(recipe -> RecipeResponseDTO.builder()
                        .id(recipe.getId())
                        .author(recipe.getAuthor().getId())
                        .category(recipe.getCategory().getId())
                        .dateOfAddition(recipe.getDateOfAddition())
                        .description(recipe.getDescription())
                        .preparationTime(recipe.getPreparationTime())
                        .title(recipe.getTitle())
                        .build()).toList();
    }
}
