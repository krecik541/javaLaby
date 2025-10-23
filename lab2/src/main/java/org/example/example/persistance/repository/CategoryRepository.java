package org.example.example.persistance.repository;

import jakarta.inject.Singleton;
import org.example.example.persistance.domain.Category;

import java.util.*;

@Singleton
public class CategoryRepository implements Repository<Category, UUID> {

    private final Map<UUID, Category> categories;

    public CategoryRepository() {
        categories = new HashMap<>();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        Category category = categories.get(id);
        if (category != null)
            return Optional.of(category);
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        return categories.values()
                .stream()
                .toList();
    }

    @Override
    public UUID create(Category category) {
        UUID id = UUID.randomUUID();
        category.setId(id);
        categories.put(id, category);
        return id;
    }

    @Override
    public UUID delete(UUID id) {
        return categories.remove(id) != null ? id : null;
    }

    @Override
    public UUID update(UUID id, Category category) {
        if (categories.containsKey(id)) {
            categories.put(id, category);
            return id;
        }
        return null;
    }
}
