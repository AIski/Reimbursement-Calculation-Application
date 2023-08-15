package pl.Alski.databaseInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClaimInitializer {
private final Logger logger = LoggerFactory.getLogger(ClaimInitializer.class);

    private final String createClaimTableSQL = "CREATE TABLE IF NOT EXISTS `CLAIM` (" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            " CREATED_AT TIMESTAMP NOT NULL," +
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
            "(ID, CREATED_AT, START_DATE, END_DATE, STATUS, TOTAL_REIMBURSEMENT_AMOUNT, USER_ID) " +
            "VALUES " +
            "(1, '2023-08-01 10:00:00', '2023-08-01', '2023-08-05', 'PENDING', 250.0, 1), " +
            "(2, '2023-08-12 11:30:00', '2023-08-12', '2023-08-17', 'APPROVED', 150.0, 2), " +
            "(3, '2023-08-05 09:45:00', '2023-08-05', '2023-08-09', 'PENDING', 180.0, 3), " +
            "(4, '2023-08-06 14:15:00', '2023-08-06', '2023-08-08', 'APPROVED', 320.0, 4)";

    private final String insertDailyAllowanceQuery = "INSERT INTO `DAILY_ALLOWANCE` " +
            "(REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "(75.0, 1), " +
            "(90.0, 2), " +
            "(75.0, 3), " +
            "(45.0, 4)";

    private final String insertCarMileageQuery = "INSERT INTO `CAR_MILEAGE` " +
            "(DISTANCE_IN_KM, CAR_PLATES, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "(200, 'ABC456', 40.0, 2), " +
            "(150, 'DEF789', 30.0, 3), ";

    private final String insertReceiptQuery = "INSERT INTO `RECEIPT` " +
            "(NAME, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES " +
            "('Taxi', 10.0, 1), " +
            "('Hotel', 80.0, 2), " +
            "('Train', 25.0, 3), " +
            "('Flight', 100.0, 4)";



    public void initialize() {
        //TODO: Add reimbursed days table + insertInto queries
        //TODO: initialize all the querries
    }
}
