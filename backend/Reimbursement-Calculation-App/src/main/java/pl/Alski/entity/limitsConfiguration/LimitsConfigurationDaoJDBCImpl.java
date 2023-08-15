package pl.Alski.entity.limitsConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LimitsConfigurationDaoJDBCImpl implements LimitsConfigurationDao {
    private final Logger logger = LoggerFactory.getLogger(LimitsConfigurationDaoJDBCImpl.class);

    @Override
    public LimitsConfiguration getConfiguration(int id) {
        LimitsConfiguration limitsConfiguration = null;
        logger.info("Getting LimitsConfiguration...");
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password")) {
            String query = "SELECT * FROM LIMITS_CONFIGURATION";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                limitsConfiguration = getLimitsConfigurationFromResultSet(resultSet);
            }
            String secondQuery = "SELECT * FROM RECEIPT_LIMITS WHERE LIMITS_CONFIGURATION_ID = " + limitsConfiguration.getId();
            Statement secondStatement = connection.createStatement();
            ResultSet secondResultSet = secondStatement.executeQuery(secondQuery);
            HashMap<String, Double> receiptLimits = new HashMap<>();
            while (secondResultSet.next()) {
                String receiptType = secondResultSet.getString("RECEIPT_TYPE");
                Double perReceiptLimit = secondResultSet.getDouble("PER_RECEIPT_LIMIT");
                receiptLimits.put(receiptType, perReceiptLimit);
            }
            limitsConfiguration.setReceiptLimits(receiptLimits);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return limitsConfiguration;
    }



    @Override
    public void saveConfiguration(LimitsConfiguration configuration) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password")) {
            String insertQuery = "INSERT INTO LIMITS_CONFIGURATION (DAILY_ALLOWANCE_RATE, CAR_MILEAGE_RATE, "
                    + "TOTAL_REIMBURSEMENT_LIMIT, MILEAGE_LIMIT_IN_KILOMETERS) "
                    + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setDouble(1, configuration.getDailyAllowanceRate());
                stmt.setDouble(2, configuration.getCarMileageRate());
                stmt.setInt(3, configuration.getTotalReimbursementLimit());
                stmt.setInt(4, configuration.getMileageLimitInKilometers());
                stmt.executeUpdate();
            }

            Map<String, Double> receiptLimits = configuration.getReceiptLimits();
            String insertReceiptQuery = "INSERT INTO RECEIPT_LIMITS (LIMITS_CONFIGURATION_ID, RECEIPT_TYPE, PER_RECEIPT_LIMIT) "
                    + "VALUES (?, ?, ?)";

            try (PreparedStatement receiptStmt = connection.prepareStatement(insertReceiptQuery)) {
                int configurationId = getLastInsertedId(connection);
                for (Map.Entry<String, Double> entry : receiptLimits.entrySet()) {
                    receiptStmt.setInt(1, configurationId);
                    receiptStmt.setString(2, entry.getKey());
                    receiptStmt.setDouble(3, entry.getValue());
                    receiptStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LimitsConfiguration getLimitsConfigurationFromResultSet(ResultSet resultSet) throws SQLException {
        LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
        logger.info("Getting ID");
        limitsConfiguration.setId(resultSet.getInt("id"));
        logger.info("Getting DailyAllowanceRate");
        limitsConfiguration.setDailyAllowanceRate(resultSet.getDouble("DAILY_ALLOWANCE_RATE"));
        limitsConfiguration.setCarMileageRate(resultSet.getDouble("CAR_MILEAGE_RATE"));
        limitsConfiguration.setTotalReimbursementLimit(resultSet.getInt("TOTAL_REIMBURSEMENT_LIMIT"));
        limitsConfiguration.setMileageLimitInKilometers(resultSet.getInt("MILEAGE_LIMIT_IN_KILOMETERS"));
        return limitsConfiguration;
    }

    private int getLastInsertedId(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT LAST_INSERT_ID()")) {
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to retrieve last inserted ID.");
    }

}
