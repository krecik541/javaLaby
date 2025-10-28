package org.example.example.persistance.domain;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recipe {
    private UUID id;
    private String title;
    private String description;
    private int preparationTime;
    private Date dateOfAddition;

    private UUID author;
    private UUID category;
}
