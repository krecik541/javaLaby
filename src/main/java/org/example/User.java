package org.example;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;

    private List<Recipe> recipes;

    public User(int id, String name, String email, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
