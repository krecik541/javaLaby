package org.example.example.persistance.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestDTO {
    private String name;
    private String email;
}
