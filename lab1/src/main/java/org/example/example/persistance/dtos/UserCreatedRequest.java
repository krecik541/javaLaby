package org.example.example.persistance.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedRequest {
    private String name;
    private String email;
}
