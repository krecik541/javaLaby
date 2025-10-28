package org.example.example.persistance.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Category {

    private UUID id;
    private String name;
    private CategoryType type;
    private List<UUID> recipes;
}
