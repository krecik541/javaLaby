package org.example.example.persistance.dtos;

import lombok.*;
import org.example.example.persistance.domain.CategoryType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryRequestDTO {

    private String name;
    private CategoryType type;
}
