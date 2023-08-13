package pl.Alski;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.controller.UserServlet;

import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = LoggerFactory.getLogger(Main.class);
        int port = 8080;
        Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        Servlet userServlet = new UserServlet();
        ServletHolder userServletHolder = new ServletHolder((Source) userServlet);
        contextHandler.addServlet(userServletHolder, "/user-record/*");
        server.setHandler(contextHandler);
        server.start();

        logger.info("Reimbursement App is up and running on port " + port + ".");
        logger.info("Press Enter to stop the server...");
        System.in.read();

        server.stop();
    }
}