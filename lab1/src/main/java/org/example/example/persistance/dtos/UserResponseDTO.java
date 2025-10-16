package org.example.example.persistance.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private boolean hasAvatar = false;
}
