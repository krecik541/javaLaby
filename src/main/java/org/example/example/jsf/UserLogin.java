package org.example.example.jsf;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.example.api.FacesElement;
import org.example.example.persistance.domain.Role;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@RequestScoped
@Named
@Getter
@Setter
public class UserLogin {

    private final HttpServletRequest request;
    private final FacesContext facesContext;
    private final SecurityContext securityContext;
    private final HttpServletResponse response;

    @Inject
    public UserLogin(HttpServletRequest request, @FacesElement HttpServletResponse response, FacesContext facesContext, SecurityContext securityContext) {
        this.request = request;
        this.response = response;
        this.facesContext = facesContext;
        this.securityContext = securityContext;
    }

    private String login;

    private String password;

    @SneakyThrows
    public void loginAction() {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(request, response, withParams().credential(credential));

        var caller = securityContext.getCallerPrincipal();
        boolean isAdminRole = request.isUserInRole(Role.ADMIN);
        System.out.println("Auth status: " + status + ", principal: " + caller + ", isAdminRole: " + isAdminRole);
        if (status == AuthenticationStatus.SUCCESS) {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/index.xhtml");
        } else {
            facesContext.responseComplete();
        }
    }

}