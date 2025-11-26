package org.example.example.api;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.example.example.controller.RecipeController;
import org.example.example.persistance.domain.Recipe;
import org.example.example.persistance.domain.Role;
import org.example.example.persistance.dtos.RecipeRequestDTO;
import org.example.example.persistance.dtos.RecipeResponseDTO;
import org.example.example.persistance.dtos.RecipeRestDTO;

import java.util.List;
import java.util.UUID;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("")
public class RecipeRESTApi {

    @Inject
    RecipeController recipeController;

    @GET
    @Path("/recipes")
    @RolesAllowed(Role.ADMIN)
    public Response listAll() {
        List<RecipeResponseDTO> all = recipeController.findAll();
        List<RecipeRestDTO> converted = all.stream().map(Converter::convert).toList();
        return Response.ok(converted).build();
    }

    @GET
    @Path("/categories/{categoryId}/recipes")
    @RolesAllowed({Role.ADMIN, Role.USER})
    public Response listByCategory(@PathParam("categoryId") String categoryId) {
        try {
            UUID cid = UUID.fromString(categoryId);
            List<RecipeResponseDTO> byCat = recipeController.findAllDtos(cid);
            List<RecipeRestDTO> all = byCat.stream().map(Converter::convert).toList();
            return Response.ok(all).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid category id").build();
        }
    }

    @GET
    @Path("/categories/{categoryId}/recipes/{recipeId}")
    @RolesAllowed({Role.ADMIN, Role.USER})
    public Response getById(@PathParam("categoryId") String categoryId, @PathParam("recipeId") String recipeId) {
        try {
            UUID cid = UUID.fromString(categoryId);
            UUID rid = UUID.fromString(recipeId);
            // retrieve recipe and ensure it belongs to category
            Recipe recipe = recipeController.findById(rid);
            if (recipe == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found").build();
            }
            if (recipe.getCategory() == null || !recipe.getCategory().getId().equals(cid)) {
                return Response.status(Response.Status.NOT_FOUND).entity("Recipe not found in this category").build();
            }
            return Response.ok(Converter.convert(recipe)).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid UUID").build();
        }
    }

    @POST
    @Path("/categories/{categoryId}/recipes")
    @RolesAllowed({Role.ADMIN, Role.USER})
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
    @RolesAllowed(Role.ADMIN)
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
    @RolesAllowed(Role.ADMIN)
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
        public static RecipeRestDTO convert(RecipeResponseDTO recipe) {
            return RecipeRestDTO.builder()
                    .id(recipe.getId())
                    .title(recipe.getTitle())
                    .description(recipe.getDescription())
                    .preparationTime(recipe.getPreparationTime())
                    .dateOfAddition(recipe.getDateOfAddition())
                    .category(UUID.fromString(recipe.getCategory()))
                    .build();
        }

        public static RecipeRestDTO convert(Recipe recipe) {
            return RecipeRestDTO.builder()
                    .id(recipe.getId())
                    .title(recipe.getTitle())
                    .description(recipe.getDescription())
                    .preparationTime(recipe.getPreparationTime())
                    .dateOfAddition(recipe.getDateOfAddition())
                    .category(recipe.getCategory().getId())
                    .build();
        }
    }
}
