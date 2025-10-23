package org.example.example.persistance.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class Recipe {
    private UUID id;
    private String title;
    private String description;
    private int preparationTime;
    private Date dateOfAddition;
    private User author;
    private Category category;

    public Recipe(UUID id, String title, String description, int preparationTime, Date dateOfAddition, User author, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.preparationTime = preparationTime;
        this.dateOfAddition = dateOfAddition;
        this.author = author;
        this.category = category;
    }
}
