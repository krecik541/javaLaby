package org.example;

import java.util.Date;

public class Recipe {
    private int id;
    private String title;
    private String description;
    private int preparationTime;
    private Date dateOfAddition;
    private User author;
    private Category category;

    public Recipe(int id, String title, String description, int preparationTime, Date dateOfAddition, User author, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.preparationTime = preparationTime;
        this.dateOfAddition = dateOfAddition;
        this.author = author;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Date getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Date dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
