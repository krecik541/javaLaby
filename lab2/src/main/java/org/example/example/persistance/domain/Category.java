package org.example.example.persistance.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Category {

    private UUID id;
    private String name;
    private CategoryType type;
    private List<Recipe> recipes;

    public Category(UUID id, String name, CategoryType type, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.recipes = recipes;
    }
}
