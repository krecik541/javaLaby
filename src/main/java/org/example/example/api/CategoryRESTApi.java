package org.example.example.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.example.controller.CategoryController;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.dtos.CategoryResponseDTO;

import java.util.List;
import java.util.UUID;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Path("")
public class CategoryRESTApi {

    @Inject
    CategoryController categoryController;

    @GET
    @Path("/categories")
    public Response listAll() {
        List<CategoryResponseDTO> all = categoryController.findAll();
        return Response.ok(all).build();
    }

    @GET
    @Path("/categories/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            CategoryResponseDTO dto = categoryController.findById(uuid);
            if (dto == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
            }
            return Response.ok(dto).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID").build();
        }
    }

    @POST
    @Path("/categories")
    public Response create(CategoryRequestDTO request) {
        try {
            UUID id = categoryController.create(request);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create category").build();
        }
    }

    @PUT
    @Path("/categories/{id}")
    public Response update(@PathParam("id") String id, CategoryRequestDTO request) {
        try {
            UUID uuid = UUID.fromString(id);
            UUID updated = categoryController.update(uuid, request);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID or update failed").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
        }
    }

    @DELETE
    @Path("/categories/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            UUID deleted = categoryController.delete(uuid);
            if (deleted == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Category not found").build();
            }
            return Response.ok(deleted).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID").build();
        }
    }
}
