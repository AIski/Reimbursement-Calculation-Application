package pl.Alski.databaseInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LimitsInitializer {

    private final Logger logger = LoggerFactory.getLogger(LimitsInitializer.class);
    private final String createLimitsConfigurationTableSQL = " CREATE TABLE LIMITS_CONFIGURATION (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
            "DAILY_ALLOWANCE_RATE DOUBLE NOT NULL, " +
            "CAR_MILEAGE_RATE DOUBLE NOT NULL," +
            "TOTAL_REIMBURSEMENT_LIMIT INT NOT NULL, " +
            "MILEAGE_LIMIT_IN_KILOMETERS INT NOT NULL)";

    private final String createReceiptLimitsTableSQL = "CREATE TABLE RECEIPT_LIMITS (" +
            "LIMITS_CONFIGURATION_ID INT NOT NULL, " +
            "RECEIPT_TYPE VARCHAR(255) NOT NULL, " +
            "PER_RECEIPT_LIMIT DOUBLE NOT NULL, " +
            "PRIMARY KEY (LIMITS_CONFIGURATION_ID, RECEIPT_TYPE), " +
            "FOREIGN KEY (LIMITS_CONFIGURATION_ID) REFERENCES LIMITS_CONFIGURATION(ID))";
    private final String insertLimitsConfigurationQuery = "INSERT INTO LIMITS_CONFIGURATION (" +
            "ID, DAILY_ALLOWANCE_RATE, CAR_MILEAGE_RATE, TOTAL_REIMBURSEMENT_LIMIT, MILEAGE_LIMIT_IN_KILOMETERS) " +
            "VALUES (0, 15, 0.3, 1000, 50000)";
    private final String insertReceiptLimitsToConfigurationQuery = "INSERT INTO RECEIPT_LIMITS (" +
            "LIMITS_CONFIGURATION_ID, RECEIPT_TYPE, PER_RECEIPT_LIMIT) " +
            "VALUES (0, 'Taxi', 50.0)," +
            " (0, 'Hotel', 100.0)," +
            " (0, 'Plane Ticket', 200.0)," +
            " (0, 'Train', 100.0)";

    public void initialize() {
        logger.info("--------------------------");
        logger.info("LimitsInitializer starting its job.");
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password");
            Statement statement = connection.createStatement();
            logger.info("Creating LIMITS_CONFIGURATION table.");
            statement.execute(createLimitsConfigurationTableSQL);
            logger.info("Pre-populating LIMITS_CONFIGURATION table.");
            statement.executeUpdate(insertLimitsConfigurationQuery);
            logger.info("LIMITS_CONFIGURATION table is ready.");

            logger.info("Creating RECEIPT_LIMITS table.");
            statement.execute(createReceiptLimitsTableSQL);
            logger.info("Pre-populating RECEIPT_LIMITS table.");
            statement.executeUpdate(insertReceiptLimitsToConfigurationQuery);
            logger.info("RECEIPT_LIMITS table is ready.");
            logger.info("LimitsInitializer finished its job.");
            logger.info("--------------------------");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

