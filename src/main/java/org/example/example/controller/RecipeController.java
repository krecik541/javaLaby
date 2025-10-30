package org.example.example.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.service.RecipeService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController() {
    }

    @Inject
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

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

    public List<RecipeResponseDTO> findAll() {
        return recipeService.findAll()
                .stream()
                .map(recipe -> RecipeResponseDTO.builder()
                        .id(recipe.getId())
                        .author(recipe.getAuthor())
                        .category(recipe.getCategory())
                        .dateOfAddition(recipe.getDateOfAddition())
                        .description(recipe.getDescription())
                        .preparationTime(recipe.getPreparationTime())
                        .title(recipe.getTitle())
                        .build())
                .collect(Collectors.toList());
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
        List<RecipeResponseDTO> r = recipeService.findAll().stream().filter(recipe -> recipe.getCategory().toString().equals(id)) .map(recipe -> RecipeResponseDTO.builder()
                .id(recipe.getId())
                .author(recipe.getAuthor())
                .category(recipe.getCategory())
                .dateOfAddition(recipe.getDateOfAddition())
                .description(recipe.getDescription())
                .preparationTime(recipe.getPreparationTime())
                .title(recipe.getTitle())
                .build()).toList();
        return r;
    }
}
