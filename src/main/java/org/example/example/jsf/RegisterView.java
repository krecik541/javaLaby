package org.example.example.jsf;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.example.example.controller.UserController;
import org.example.example.persistance.dtos.UserRequestDTO;

import java.io.Serializable;

@Named("registerView")
@ViewScoped
@Getter
@Setter
public class RegisterView implements Serializable {

    @Inject
    private UserController userController;

    private String name;
    private String login;
    private String email;
    private String password;
    private String confirmPassword;
    private String errorMessage;

    public String register() {
        try {
            // Validate inputs
            if (name == null || name.trim().isEmpty()) {
                errorMessage = "Name is required";
                return null;
            }
            if (login == null || login.trim().isEmpty()) {
                errorMessage = "Login is required";
                return null;
            }
            if (email == null || email.trim().isEmpty()) {
                errorMessage = "Email is required";
                return null;
            }
            if (password == null || password.trim().isEmpty()) {
                errorMessage = "Password is required";
                return null;
            }
            if (!password.equals(confirmPassword)) {
                errorMessage = "Passwords do not match";
                return null;
            }

            UserRequestDTO dto = UserRequestDTO.builder()
                    .name(name)
                    .login(login)
                    .email(email)
                    .password(password)
                    .build();

            userController.create(dto);
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            errorMessage = "Registration failed: " + e.getMessage();
            return null;
        }
    }
}
