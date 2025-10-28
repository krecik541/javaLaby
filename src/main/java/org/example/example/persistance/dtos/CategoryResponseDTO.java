package org.example.example.persistance.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.example.persistance.domain.CategoryType;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private CategoryType type;
    private List<UUID> recipes;
}
