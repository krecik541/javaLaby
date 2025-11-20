package org.example.example.persistance.domain;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String login;
    private String name;
    private String email;

    private String password;

    @Transient
    private Path avatar;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Recipe> recipes;

    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    private List<String> roles;

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
