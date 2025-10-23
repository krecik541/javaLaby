package org.example.example.api;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.example.controller.CategoryController;
import org.example.example.persistance.dtos.CategoryRequestDTO;
import org.example.example.persistance.dtos.CategoryResponseDTO;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/category/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class ApiCategory extends HttpServlet {

    private final Jsonb jsonb = JsonbBuilder.create();
    private final CategoryController categoryController;

    @Inject
    public ApiCategory(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        String path = getUUIDFromPath(req);

        if (path == null || path.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonb.toJson(categoryController.findAll()));
        } else {
            try {
                UUID id = UUID.fromString(path);
                CategoryResponseDTO user = categoryController.findById(id);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonb.toJson(user));
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ERROR: invalid UUID format");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("ERROR: category not found");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            CategoryRequestDTO dto = jsonb.fromJson(req.getInputStream(), CategoryRequestDTO.class);
            UUID uuid = categoryController.create(dto);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(jsonb.toJson(uuid));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            CategoryRequestDTO dto = jsonb.fromJson(req.getInputStream(), CategoryRequestDTO.class);
            UUID uuid = categoryController.create(dto);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(jsonb.toJson(uuid));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            UUID id = UUID.fromString(getUUIDFromPath(req));
            categoryController.delete(id);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("User deleted successfully");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("ERROR: user not found or deletion failed");
        }
    }


    private String getUUIDFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            return null;
        }
        return pathInfo.substring(1);
    }
}
