package org.example.example.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.example.example.controller.UserController;
import org.example.example.persistance.dtos.UserResponseDTO;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Named("usersView")
public class UserFormView implements Serializable {
    @EJB
    private UserController userController;

    private List<UserResponseDTO> users;

    @PostConstruct
    public void init() {
        users = userController.findAll();
    }

    public List<UserResponseDTO> getUsers() {
        return users;
    }

    public void checkAccess() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        var req = fc.getExternalContext().getRequest();
        if (fc.getExternalContext().getUserPrincipal() == null) {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath() + "/authentication/login.xhtml");
        }
    }

    public String delete(UUID id) {
        userController.delete(id);
        init();
        return null;
    }
}
