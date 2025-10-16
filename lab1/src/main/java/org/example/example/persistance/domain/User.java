package org.example.example.persistance.domain;

import lombok.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String email;
    private byte[] avatar;

    private List<Recipe> recipes;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", recipes=" + recipes +
                '}';
    }
}
