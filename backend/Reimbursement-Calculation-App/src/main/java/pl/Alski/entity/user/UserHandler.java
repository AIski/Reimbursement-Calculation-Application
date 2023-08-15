package pl.Alski.entity.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UserHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    private final UserDao userRepository = new UserDaoJDBCImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/users") && method.equals("GET")) {
            handleGetUsers(exchange);
        } else if (path.equals("/users") && method.equals("POST")) {
            handlePostUsers(exchange);
        }
    }

    private void handleGetUsers(HttpExchange exchange) throws IOException {
        List<User> users = userRepository.getUsers();
        String jsonResponse = serializeUsersToJson(users);
        exchange.getResponseHeaders().set("Content-Type", "application/JSON");
        if (jsonResponse.length() != 0) {
            exchange.sendResponseHeaders(200, jsonResponse.length());
        } else exchange.sendResponseHeaders(404, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void handlePostUsers(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        User newUser = deserializeUserFromJson(requestBody);
        userRepository.saveUser(newUser);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, -1);
        exchange.getResponseBody().close();
    }

    private User deserializeUserFromJson(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, User.class);
    }

    private String serializeUsersToJson(List<User> users) throws JsonProcessingException {
        return objectMapper.writeValueAsString(users);
    }
}
