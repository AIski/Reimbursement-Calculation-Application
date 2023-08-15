package pl.Alski.entity.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import pl.Alski.entity.user.UserDao;
import pl.Alski.entity.user.UserDaoJDBCImpl;
import pl.Alski.entity.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {

    private UserDao userRepository = new UserDaoJDBCImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String UserID = request.getParameter("id");
        if (request != null) {
            int id = Integer.parseInt(UserID);
            User tempUser = userRepository.getUserById(id);
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
        //TODO: this code.
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
