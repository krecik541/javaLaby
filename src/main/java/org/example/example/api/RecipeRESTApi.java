package org.example.example.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;

import java.util.List;
import java.util.UUID;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("1")
public class RecipeRESTApi {

    @Inject
    RecipeController recipeController;

    // List all recipes (global)
    @GET
    @Path("/recipes")
    public Response listAll() {
        List<RecipeResponseDTO> all = recipeController.findAll();
        return Response.ok(all).build();
    }

    // Hierarchical: list recipes in a category
    @GET
    @Path("/categories/{categoryId}/recipes")
    public Response listByCategory(@PathParam("categoryId") String categoryId) {
        try {
            UUID cid = UUID.fromString(categoryId);
            List<RecipeResponseDTO> byCat = recipeController.findByCategory(cid.toString());
            return Response.ok(byCat).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid category id").build();
        }
    }

    @GET
    @Path("/categories/{categoryId}/recipes/{recipeId}")
    public Response getById(@PathParam("categoryId") String categoryId, @PathParam("recipeId") String recipeId) {
        try {
            UUID cid = UUID.fromString(categoryId);
            UUID rid = UUID.fromString(recipeId);
            // retrieve recipe and ensure it belongs to category
            var recipe = recipeController.findById(rid);
            if (recipe == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found").build();
            }
            if (recipe.getCategory() == null || !recipe.getCategory().equals(cid)) {
                return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found in this category").build();
            }
            return Response.ok(recipe).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID").build();
        }
    }

    @POST
    @Path("/categories/{categoryId}/recipes")
    public Response create(@PathParam("categoryId") String categoryId, RecipeRequestDTO request) {
        try {
            UUID cid = UUID.fromString(categoryId);
            // enforce category from path
            request.setCategory(cid);
            UUID id = recipeController.create(request);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid category id or request").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create recipe").build();
        }
    }

    @PUT
    @Path("/categories/{categoryId}/recipes/{recipeId}")
    public Response update(@PathParam("categoryId") String categoryId, @PathParam("recipeId") String recipeId, RecipeRequestDTO request) {
        try {
            UUID cid = UUID.fromString(categoryId);
            UUID rid = UUID.fromString(recipeId);
            // enforce category from path
            request.setCategory(cid);
            UUID updated = recipeController.update(rid, request);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID or request").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found").build();
        }
    }

    @DELETE
    @Path("/categories/{categoryId}/recipes/{recipeId}")
    public Response delete(@PathParam("categoryId") String categoryId, @PathParam("recipeId") String recipeId) {
        try {
            UUID rid = UUID.fromString(recipeId);
            UUID deleted = recipeController.delete(rid);
            if (deleted == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found").build();
            }
            return Response.ok(deleted).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID").build();
        }
    }

    static class Converter {

    }
}
