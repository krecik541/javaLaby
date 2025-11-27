package org.example.example.jsf;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;

@RequestScoped
@Named("userLogout")
public class UserLogout {

    @Inject
    private HttpServletRequest request;

    @Inject
    private FacesContext facesContext;

    @SneakyThrows
    public String logout() {
        try {
            request.logout();
        } catch (Exception ignored) {}
        facesContext.getExternalContext().invalidateSession();
        return "redirect:/index.xhtml";
    }
}
