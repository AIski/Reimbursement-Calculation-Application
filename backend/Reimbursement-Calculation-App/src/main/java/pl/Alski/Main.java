package pl.Alski;

import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.databaseInitializer.DatabaseInitializer;
import pl.Alski.entity.claim.ReimbursementClaimHandler;
import pl.Alski.entity.limitsConfiguration.LimitsConfigurationHandler;

import java.net.InetSocketAddress;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
private static final int port = 8080;
    public static void main(String[] args) throws Exception {
       logger.info("Starting Reimbursement App...");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
//        server.createContext("/users", new UserHandler());
        server.createContext("/limits_config", new LimitsConfigurationHandler());
        server.createContext("/claim", new ReimbursementClaimHandler());
        server.start();
        DatabaseInitializer dbInitializer = new DatabaseInitializer();
        dbInitializer.initialize();
        logger.info("Reimbursement App is up and running on port " + port + ".");
        logger.info("Press Enter to stop the server...");
        System.in.read();
        server.stop(0);
    }
}