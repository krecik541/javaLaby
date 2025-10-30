package org.example.example.persistance.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.example.persistance.domain.Recipe;

import java.util.*;

@ApplicationScoped
public class RecipeRepository implements Repository<Recipe, UUID> {

    private final Map<UUID, Recipe> recipes;

    public RecipeRepository() {
        this.recipes = new HashMap<>();
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        Recipe recipe = recipes.get(id);
        if (recipe != null)
            return Optional.of(recipe);
        return Optional.empty();
    }

    @Override
    public List<Recipe> findAll() {
        return recipes.values()
                .stream()
                .toList();
    }

    @Override
    public UUID create(Recipe recipe) {
        UUID id = UUID.randomUUID();
        recipe.setId(id);
        recipes.put(id, recipe);
        return id;
    }

    @Override
    public UUID delete(UUID id) {
        return recipes.remove(id) != null ? id : null;
    }

    @Override
    public UUID update(UUID id, Recipe recipe) {
        if (recipes.containsKey(id)) {
            recipes.put(id, recipe);
            return id;
        }
        return null;
    }

    public void deleteByCategory(UUID id) {
        for(Map.Entry<UUID, Recipe> recipe : recipes.entrySet()) {
            Recipe r = recipe.getValue();
            if(r.getCategory() == id) {
                recipes.remove(recipe.getKey());
            }
        }
    }
}
