package org.example;

import java.util.Date;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private CategoryType type;
    private List<Recipe> recipes;

    public Category(int id, String name, CategoryType type, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.recipes = recipes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
