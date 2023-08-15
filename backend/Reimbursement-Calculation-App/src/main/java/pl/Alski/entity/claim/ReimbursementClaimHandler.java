package pl.Alski.entity.claim;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbursementClaimHandler implements HttpHandler {
    private final ReimbursementClaimDao claimRepository = new ReimbursementClaimDaoJDBCImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/claim") && method.equals("GET")) {
            handleGetClaims(exchange);
        } else if (path.equals("/claim") && method.equals("POST")) {
            handlePostClaim(exchange);
        }
    }

    private void handlePostClaim(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        ReimbursementClaim claim = deserializeClaimFromJson(requestBody);
        claimRepository.saveClaim(claim);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, -1);
        exchange.getResponseBody().close();
    }

    private void handleGetClaims(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQuery(query);
        Integer userId = Integer.parseInt(queryParams.get("userId"));

        List<ReimbursementClaim> userClaims = claimRepository.getClaimsByUserId(userId);
        String jsonResponse = serializeClaimToJson(userClaims);
        exchange.getResponseHeaders().set("Content-Type", "application/JSON");

        if (jsonResponse.length() != 0) {
            exchange.sendResponseHeaders(200, jsonResponse.length());
        } else exchange.sendResponseHeaders(404, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    queryParams.put(key, value);
                }
            }
        }
        return queryParams;
    }

    private ReimbursementClaim deserializeClaimFromJson(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, ReimbursementClaim.class);
    }

    private String serializeClaimToJson(List<ReimbursementClaim> claims) throws JsonProcessingException {
        return objectMapper.writeValueAsString(claims);
    }
}