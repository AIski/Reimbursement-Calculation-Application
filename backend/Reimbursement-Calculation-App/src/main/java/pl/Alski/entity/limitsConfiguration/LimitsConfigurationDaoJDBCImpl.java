package pl.Alski.entity.limitsConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LimitsConfigurationDaoJDBCImpl implements LimitsConfigurationDao {
    private final Logger logger = LoggerFactory.getLogger(LimitsConfigurationDaoJDBCImpl.class);
    private static final String DB_URL = "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";
    private final String updateConfigurationQuery = "UPDATE LIMITS_CONFIGURATION " +
            "SET DAILY_ALLOWANCE_RATE = ?, CAR_MILEAGE_RATE = ?, " +
            "TOTAL_REIMBURSEMENT_LIMIT = ?, MILEAGE_LIMIT_IN_KILOMETERS = ?";

    private final String deleteReceiptLimitsQuery = "DELETE FROM RECEIPT_LIMITS";
    private final String insertReceiptLimitQuery = "INSERT INTO RECEIPT_LIMITS (LIMITS_CONFIGURATION_ID, RECEIPT_TYPE, PER_RECEIPT_LIMIT) " +
            "VALUES (?, ?, ?)";
    private final String getLimitsConfigurationQuery = "SELECT * FROM LIMITS_CONFIGURATION";
    private final String getReceiptLimitsQuery = "SELECT * FROM RECEIPT_LIMITS WHERE LIMITS_CONFIGURATION_ID = ?";

    @Override
    public LimitsConfiguration getConfiguration() {
        LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
        logger.info("Getting LimitsConfiguration...");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getLimitsConfigurationQuery);
            while (resultSet.next()) {
                limitsConfiguration = getLimitsConfigurationFromResultSet(resultSet);
                loadReceiptLimits(limitsConfiguration, connection);
            }
            logger.info("Successfully fetched LimitsConfiguration from db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return limitsConfiguration;
    }


    @Override
    public void saveConfiguration(LimitsConfiguration configuration) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            updateConfiguration(configuration, connection);
            deleteOldReceiptLimits(connection);
            Map<String, Double> receiptLimits = configuration.getReceiptLimits();
            insertNewReceiptLimits(configuration, connection, receiptLimits);

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadReceiptLimits(LimitsConfiguration limitsConfiguration, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(getReceiptLimitsQuery)) {
            statement.setInt(1, limitsConfiguration.getId());
            ResultSet receiptLimitsResultSet = statement.executeQuery();
            HashMap<String, Double> receiptLimits = new HashMap<>();
            while (receiptLimitsResultSet.next()) {
                String receiptType = receiptLimitsResultSet.getString("RECEIPT_TYPE");
                Double perReceiptLimit = receiptLimitsResultSet.getDouble("PER_RECEIPT_LIMIT");
                receiptLimits.put(receiptType, perReceiptLimit);
            }
            limitsConfiguration.setReceiptLimits(receiptLimits);
        }
    }


    private void updateConfiguration(LimitsConfiguration configuration, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(updateConfigurationQuery)) {
            statement.setDouble(1, configuration.getDailyAllowanceRate());
            statement.setDouble(2, configuration.getCarMileageRate());
            statement.setInt(3, configuration.getTotalReimbursementLimit());
            statement.setInt(4, configuration.getMileageLimitInKilometers());
            statement.executeUpdate();
        }
    }

    private void deleteOldReceiptLimits(Connection connection) throws SQLException {
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteReceiptLimitsQuery)) {
            deleteStatement.executeUpdate();
        }
    }

    private void insertNewReceiptLimits(LimitsConfiguration configuration, Connection connection, Map<String, Double> receiptLimits) throws SQLException {
        try (PreparedStatement receiptStmt = connection.prepareStatement(insertReceiptLimitQuery)) {
            int configurationId = configuration.getId();
            for (Map.Entry<String, Double> entry : receiptLimits.entrySet()) {
                receiptStmt.setInt(1, configurationId);
                receiptStmt.setString(2, entry.getKey());
                receiptStmt.setDouble(3, entry.getValue());
                receiptStmt.executeUpdate();
            }
        }
    }

    private LimitsConfiguration getLimitsConfigurationFromResultSet(ResultSet resultSet) throws SQLException {
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


}
