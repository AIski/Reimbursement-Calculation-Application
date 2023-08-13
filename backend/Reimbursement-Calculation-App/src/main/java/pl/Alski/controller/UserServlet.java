package pl.Alski.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import pl.Alski.DAO.UserDao;
import pl.Alski.DAO.UserDaoJPAImpl;
import pl.Alski.entity.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor
@WebServlet(name = "UserServlet", urlPatterns = "/user-record")
public class UserServlet extends HttpServlet {

    private UserDao userService = new UserDaoJPAImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String UserID = request.getParameter("id");
        if (request != null) {
            int id = Integer.parseInt(UserID);
            User tempUser = userService.getUserById(id);
            if (tempUser != null) {
                response.setContentType("application/JSON");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), tempUser);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = objectMapper.readValue(request.getReader(), User.class);
        // Handle creation logic
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle update logic
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle deletion logic
    }

}
