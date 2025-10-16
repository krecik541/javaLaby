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

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/user/avatar/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class ApiAvatar extends HttpServlet {

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

        if (!path.isEmpty()){
            UUID id = UUID.fromString(path);
            try {
                byte[] file = userController.getAvatar(id);
                if(file == null)
                    resp.getWriter().write("User does not have an avatar");
                else {
                    resp.setContentType("img/png");
                    resp.setStatus(HttpServletResponse.SC_FOUND);
                    resp.setContentLength(file.length);
                    resp.getOutputStream().write(file);
                }
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("ERROR: user not found");
            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: user not found");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println(-1);
        UUID id = UUID.fromString(getUUIDFromPath(req));
        System.out.println(0.5);
        try {
            id = userController.setAvatar(id, req.getPart("avatar").getInputStream());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonb.toJson(id));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UUID id = UUID.fromString(getUUIDFromPath(req));
        try {
            id = userController.setAvatar(id, req.getPart("avatar").getInputStream());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonb.toJson(id));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: something went wrong");
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UUID id = UUID.fromString(getUUIDFromPath(req));
        userController.deleteAvatar(id);
    }



    private String getUUIDFromPath(HttpServletRequest req) {
        return req.getPathInfo().substring(1);
    }
}
