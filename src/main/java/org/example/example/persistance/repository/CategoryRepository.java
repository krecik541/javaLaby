package org.example.example.persistance.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.example.persistance.domain.Category;
import org.example.example.persistance.domain.CategoryType;

import java.util.*;

@ApplicationScoped
public class CategoryRepository implements Repository<Category, UUID> {

    private final Map<UUID, Category> categories;

    public CategoryRepository() {
        categories = new HashMap<>();
        // Add some sample data
        Category breakfast = Category.builder()
                .name("Śniadania")
                .type(CategoryType.BREAKFAST)
                .recipes(new ArrayList<>())
                .build();
        Category dinner = Category.builder()
                .name("Obiady")
                .type(CategoryType.DINNER)
                .recipes(new ArrayList<>())
                .build();
        create(breakfast);
        create(dinner);
        System.out.println("CategoryRepository initialized with " + categories.size() + " sample categories");
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
        List<Category> result = categories.values()
                .stream()
                .toList();
        System.out.println("CategoryRepository.findAll() returning " + result.size() + " categories");
        return result;
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
