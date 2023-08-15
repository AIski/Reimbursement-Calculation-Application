package pl.Alski.entity.limitsConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LimitsConfigurationServlet {
    private LimitsConfigurationDao limitsConfigurationRepository = new LimitsConfigurationDaoJDBCImpl();


    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String configurationId = request.getParameter("id");
        if (request != null) {
            int id = Integer.parseInt(configurationId);
            LimitsConfiguration configuration = limitsConfigurationRepository.getConfiguration(id);
            if (configuration != null) {
                response.setContentType("application/JSON");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), configuration);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }


    public synchronized void doPost(HttpServletRequest request, HttpServletResponse response) {
        //TODO: implementation code
    }
}
