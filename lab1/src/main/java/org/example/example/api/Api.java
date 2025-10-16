package org.example.example.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.example.controller.UserController;
import org.example.example.persistance.domain.User;
import org.example.example.persistance.dtos.UserCreatedRequest;
import org.example.example.persistance.dtos.UserCreatedResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/user/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class Api extends HttpServlet {

    private final Jsonb jsonb = JsonbBuilder.create();
    private UserController userController;

    public void init(ServletConfig config) throws ServletException {
        userController = UserController.getInstance();
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");

        String path = getUUIDFromPath(req);

        if (path.isEmpty()) {
            resp.getWriter().write(jsonb.toJson(userController.findAll()));
        } else {
            UUID id = UUID.fromString(path);
            try {
                User user = userController.findById(id);
                resp.setStatus(HttpServletResponse.SC_FOUND);
                resp.getWriter().write(jsonb.toJson(user));
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("ERROR: user not found");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            UserCreatedRequest dto = jsonb.fromJson(req.getInputStream(), UserCreatedRequest.class);
            UUID uuid = userController.create(dto);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(jsonb.toJson(uuid));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            UserCreatedRequest dto = jsonb.fromJson(req.getInputStream(), UserCreatedRequest.class);
            UUID id = UUID.fromString(getUUIDFromPath(req));

            id = userController.update(id, dto);


        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UUID id = UUID.fromString(getUUIDFromPath(req));
        userController.delete(id);
    }



    private String getUUIDFromPath(HttpServletRequest req) {
        return req.getPathInfo().substring(1);
    }
}
