package org.example.example.persistance.domain;

import lombok.*;

import java.nio.file.Path;
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
    private Path avatar;

    private List<UUID> recipes;

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
