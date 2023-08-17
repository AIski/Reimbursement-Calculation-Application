package pl.Alski.databaseInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ClaimInitializer {
private final Logger logger = LoggerFactory.getLogger(ClaimInitializer.class);

    private final String createClaimTableSQL = "CREATE TABLE IF NOT EXISTS `CLAIM` (" +
            "ID INT AUTO_INCREMENT PRIMARY KEY," +
            " START_DATE DATE NOT NULL," +
            " END_DATE DATE NOT NULL," +
            " STATUS VARCHAR(50) NOT NULL," +
            " TOTAL_REIMBURSEMENT_AMOUNT DOUBLE NOT NULL," +
            " USER_ID INT NOT NULL," +
            " FOREIGN KEY (USER_ID) REFERENCES `USER`(ID))";

    private final String createDailyAllowanceTableSQL = "CREATE TABLE IF NOT EXISTS `DAILY_ALLOWANCE` (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            " REIMBURSED_AMOUNT DOUBLE NOT NULL," +
            " CLAIM_ID INT NOT NULL," +
            " FOREIGN KEY (CLAIM_ID) REFERENCES `CLAIM`(ID))";

    private final String createReimbursedDaysTableSQL = "CREATE TABLE REIMBURSED_DAYS (" +
            "ALLOWANCE_ID INT NOT NULL, " +
            "REIMBURSED_DAY DATE NOT NULL, " +
            "FOREIGN KEY (ALLOWANCE_ID) REFERENCES DAILY_ALLOWANCE (ID))";

    private final String createCarMileageTableSQL = "CREATE TABLE IF NOT EXISTS `CAR_MILEAGE` (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            " DISTANCE_IN_KM INT NOT NULL," +
            " CAR_PLATES VARCHAR(50) NOT NULL," +
            " REIMBURSED_AMOUNT DOUBLE NOT NULL," +
            " CLAIM_ID INT NOT NULL," +
            " FOREIGN KEY (CLAIM_ID) REFERENCES `CLAIM`(ID))";

    private final String createReceiptTableSQL = "CREATE TABLE IF NOT EXISTS `RECEIPT` (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            " NAME VARCHAR(255) NOT NULL," +
            " REIMBURSED_AMOUNT DOUBLE NOT NULL," +
            " CLAIM_ID INT NOT NULL," +
            " FOREIGN KEY (CLAIM_ID) REFERENCES `CLAIM`(ID))";

    private final String insertClaimsQuery = "INSERT INTO `CLAIM` " +
            "(START_DATE, END_DATE, STATUS, TOTAL_REIMBURSEMENT_AMOUNT, USER_ID) " +
            "VALUES " +
            "('2023-08-01', '2023-08-05', 'PENDING', 250.0, 1), " +
            "('2023-08-12', '2023-08-17', 'APPROVED', 150.0, 2), " +
            "('2023-08-05', '2023-08-09', 'PENDING', 180.0, 3), " +
            "('2023-08-06', '2023-08-08', 'APPROVED', 320.0, 4)";

    private final String insertDailyAllowanceQuery = "INSERT INTO `DAILY_ALLOWANCE` " +
            "(REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "(75.0, 1), " +
            "(90.0, 2), " +
            "(75.0, 3), " +
            "(45.0, 4)";


    private final String insertCarMileageQuery = "INSERT INTO CAR_MILEAGE " +
            "(DISTANCE_IN_KM, CAR_PLATES, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "(200, 'ABC456', 40.0, 2), " +
            "(150, 'DEF789', 30.0, 3) ";

    private final String insertReceiptQuery = "INSERT INTO `RECEIPT` " +
            "(NAME, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "('Taxi', 10.0, 1), " +
            "('Hotel', 80.0, 2), " +
            "('Train', 25.0, 3), " +
            "('Flight', 100.0, 4)";



    public void initialize() {
        logger.info("--------------------------");
        logger.info("ClaimInitializer starting its job.");
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password");
            Statement statement = connection.createStatement();

            logger.info("Creating CLAIM table.");
            statement.execute(createClaimTableSQL);
//            statement.executeUpdate(insertClaimsQuery);

            logger.info("Creating DAILY_ALLOWANCE table.");
            statement.execute(createDailyAllowanceTableSQL);
//            statement.executeUpdate(insertDailyAllowanceQuery);

            logger.info("Creating REIMBURSED_DAYS table.");
            statement.execute(createReimbursedDaysTableSQL);

            logger.info("Creating CAR_MILEAGE table.");
            statement.execute(createCarMileageTableSQL);
//            statement.executeUpdate(insertCarMileageQuery);

            logger.info("Creating RECEIPT table.");
            statement.execute(createReceiptTableSQL);
//            statement.executeUpdate(insertReceiptQuery);

            logger.info("Tables are ready.");
            logger.info("ClaimInitializer finished its job.");
            logger.info("--------------------------");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
