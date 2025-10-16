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
import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/user/avatar/*")
@MultipartConfig(maxFileSize = 1024 * 1024)
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

        String path = getUUIDFromPath(req);

        if (path != null && !path.isEmpty()) {
            try {
                UUID id = UUID.fromString(path);
                byte[] file = userController.getAvatar(id);
                
                if (file == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.setContentType("application/json");
                    resp.getWriter().write("User does not have an avatar");
                } else {
                    resp.setContentType("image/png");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentLength(file.length);
                    resp.getOutputStream().write(file);
                }
            } catch (IllegalArgumentException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("ERROR: invalid UUID format");
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json");
                resp.getWriter().write("ERROR: user not found");
            } 
        }
        else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("ERROR: invalid UUID in path");
        }        
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        String path = getUUIDFromPath(req);
        
        if (path == null || path.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: invalid UUID in path");
            return;
        }

        try {
            UUID id = UUID.fromString(path);
            
            if (req.getPart("avatar") == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ERROR: avatar file is required");
                return;
            }
            id = userController.setAvatar(id, req.getPart("avatar").getInputStream());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonb.toJson(id));
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: invalid UUID format");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: failed to upload avatar");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        String path = getUUIDFromPath(req);
        
        if (path == null || path.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: invalid UUID in path");
            return;
        }

        try {
            UUID id = UUID.fromString(path);
            
            if (req.getPart("avatar") == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ERROR: avatar file is required");
                return;
            }
            
            id = userController.setAvatar(id, req.getPart("avatar").getInputStream());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonb.toJson(id));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: failed to update avatar");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        
        try {
            UUID id = UUID.fromString(getUUIDFromPath(req));
            userController.deleteAvatar(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Avatar deleted successfully");
        }catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("ERROR: user not found or avatar deletion failed");
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
