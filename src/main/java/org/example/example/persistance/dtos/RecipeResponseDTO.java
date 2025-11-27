package org.example.example.persistance.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.example.persistance.domain.Recipe;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class RecipeResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private int preparationTime;
    private Date dateOfAddition;

    private UUID author;
    private UUID category;
}
