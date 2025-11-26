package org.example.example.api;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.example.controller.UserController;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.dtos.UserRequestDTO;
import org.example.example.persistance.dtos.UserResponseDTO;

import java.util.List;
import java.util.UUID;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("")
public class UserRESTApi {
    @Inject
    private UserController userController;

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<UserResponseDTO> users = userController.findAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("/users/{id}")
    public Response getUser(@PathParam("id") UUID id) {
        UserResponseDTO user = userController.findById(id);

        if(user != null) {
            return Response.ok(user).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/users")
    @PermitAll
    public Response create(UserRequestDTO user) {
        UUID id = userController.create(user);
        if (id != null)
            return Response.ok(userController.create(user)).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/users/{id}")
    public Response update(@PathParam("id") UUID id, UserRequestDTO user) {
        try {
            UUID updated = userController.update(id, user);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/users/{id}")
    public Response delete(@PathParam("id") UUID id) {
        try {
            UUID deleted = userController.delete(id);
            return Response.ok(deleted).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
