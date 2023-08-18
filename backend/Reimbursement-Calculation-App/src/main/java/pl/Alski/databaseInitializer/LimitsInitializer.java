package pl.Alski.databaseInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;
import pl.Alski.entity.limitsConfiguration.LimitsConfigurationDao;
import pl.Alski.entity.limitsConfiguration.LimitsConfigurationDaoJDBCImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class LimitsInitializer {
    public static final LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
    private final Logger logger = LoggerFactory.getLogger(LimitsInitializer.class);
    private final String createLimitsConfigurationTableSQL = " CREATE TABLE IF NOT EXISTS LIMITS_CONFIGURATION (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT, " +
            "DAILY_ALLOWANCE_RATE DOUBLE NOT NULL, " +
            "CAR_MILEAGE_RATE DOUBLE NOT NULL," +
            "TOTAL_REIMBURSEMENT_LIMIT INT NOT NULL, " +
            "MILEAGE_LIMIT_IN_KILOMETERS INT NOT NULL)";

    private final String createReceiptLimitsTableSQL = "CREATE TABLE IF NOT EXISTS RECEIPT_LIMITS (" +
            "LIMITS_CONFIGURATION_ID INT NOT NULL, " +
            "RECEIPT_TYPE VARCHAR(255) NOT NULL, " +
            "PER_RECEIPT_LIMIT DOUBLE NOT NULL, " +
            "PRIMARY KEY (LIMITS_CONFIGURATION_ID, RECEIPT_TYPE), " +
            "FOREIGN KEY (LIMITS_CONFIGURATION_ID) REFERENCES LIMITS_CONFIGURATION(ID))";

    public void initialize() {
        logger.info("--------------------------");
        logger.info("LimitsInitializer starting its job.");
        try (Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password")){
            Statement statement = connection.createStatement();
            logger.info("Creating LIMITS_CONFIGURATION table.");
            statement.execute(createLimitsConfigurationTableSQL);

            logger.info("Creating RECEIPT_LIMITS table.");
            statement.execute(createReceiptLimitsTableSQL);

            logger.info("Tables are ready.");
            limitsConfiguration.setDailyAllowanceRate(15.0);
            limitsConfiguration.setCarMileageRate(0.3);
            limitsConfiguration.setTotalReimbursementLimit(1000);
            limitsConfiguration.setMileageLimitInKilometers(50000);
            HashMap<String, Double> receiptLimits = new HashMap<>();
            receiptLimits.put("Taxi", 50.0);
            receiptLimits.put("Hotel", 100.0);
            receiptLimits.put("Plane Ticket", 200.0);
            receiptLimits.put("Train", 100.0);
            limitsConfiguration.setReceiptLimits(receiptLimits);

            LimitsConfigurationDao limitsConfigurationDao= new LimitsConfigurationDaoJDBCImpl();
            limitsConfigurationDao.insertConfiguration(limitsConfiguration);

            logger.info("LimitsInitializer finished its job.");
            logger.info("--------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

