package org.example.example.persistance.dtos;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequestDTO {
    private String title;
    private String description;
    private int preparationTime;

    private UUID author;
    private UUID category;
}
