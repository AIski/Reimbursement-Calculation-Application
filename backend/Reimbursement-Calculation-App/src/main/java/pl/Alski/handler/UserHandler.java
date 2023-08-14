package pl.Alski.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.Alski.DAO.UserDao;
import pl.Alski.DAO.UserDaoJDBCImpl;
import pl.Alski.entity.user.User;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UserHandler implements HttpHandler {
    private final UserDao userRepository = new UserDaoJDBCImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/users") && method.equals("GET")) {
            List<User> users = userRepository.getUsers();
            String jsonResponse = serializeUsersToJson(users);
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else if (path.equals("/users") && method.equals("POST")) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            User newUser = deserializeUserFromJson(requestBody);
            userRepository.saveUser(newUser);
            exchange.sendResponseHeaders(201, -1);
            exchange.getResponseBody().close();
        }
    }

    private User deserializeUserFromJson(String requestBody) throws JsonProcessingException {
       return objectMapper.readValue(requestBody, User.class);
    }

    private String serializeUsersToJson(List<User> users) throws JsonProcessingException {
        return objectMapper.writeValueAsString(users);
    }
}
