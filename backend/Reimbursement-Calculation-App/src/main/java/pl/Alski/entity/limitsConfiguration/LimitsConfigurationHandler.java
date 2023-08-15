package pl.Alski.entity.limitsConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class LimitsConfigurationHandler implements HttpHandler {
    private final LimitsConfigurationDao configurationRepository = new LimitsConfigurationDaoJDBCImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/limits_config") && method.equals("GET")) {
            handleGetLimitsConfig(exchange);
        } else if (path.equals("/limits_config") && method.equals("POST")) {
            handlePostLimitsConfig(exchange);
        }
    }

    private void handlePostLimitsConfig(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        LimitsConfiguration limitsConfiguration = deserializeConfigurationFromJson(requestBody);
        configurationRepository.saveConfiguration(limitsConfiguration);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, -1);
        exchange.getResponseBody().close();
    }

    private void handleGetLimitsConfig(HttpExchange exchange) throws IOException {
        LimitsConfiguration configuration = configurationRepository.getConfiguration(0);
        String jsonResponse = serializeConfigToJson(configuration);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        if(jsonResponse.length()!=0){
            exchange.sendResponseHeaders(200, jsonResponse.length());
        }
        else exchange.sendResponseHeaders(404, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private LimitsConfiguration deserializeConfigurationFromJson(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, LimitsConfiguration.class);
    }

    private String serializeConfigToJson(LimitsConfiguration config) throws JsonProcessingException {
        return objectMapper.writeValueAsString(config);
    }
}
