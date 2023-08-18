package pl.Alski.entity.limitsConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LimitsConfigurationDaoJDBCImpl implements LimitsConfigurationDao {
    private static final LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
    private final Logger logger = LoggerFactory.getLogger(LimitsConfigurationDaoJDBCImpl.class);
    private static final String DB_URL = "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";
    private final String updateConfigurationQuery = "UPDATE LIMITS_CONFIGURATION " +
            "SET DAILY_ALLOWANCE_RATE = ?, CAR_MILEAGE_RATE = ?, " +
            "TOTAL_REIMBURSEMENT_LIMIT = ?, MILEAGE_LIMIT_IN_KILOMETERS = ?";
    private final String insertConfigurationQuery = "INSERT INTO LIMITS_CONFIGURATION " +
            "(DAILY_ALLOWANCE_RATE, CAR_MILEAGE_RATE, TOTAL_REIMBURSEMENT_LIMIT, MILEAGE_LIMIT_IN_KILOMETERS) " +
            "VALUES (?, ?, ?, ?)";

    private final String deleteReceiptLimitsQuery = "DELETE FROM RECEIPT_LIMITS";
    private final String insertReceiptLimitQuery = "INSERT INTO RECEIPT_LIMITS (LIMITS_CONFIGURATION_ID, RECEIPT_TYPE, PER_RECEIPT_LIMIT) " +
            "VALUES (1, ?, ?)";
    private final String getLimitsConfigurationQuery = "SELECT * FROM LIMITS_CONFIGURATION";
    private final String getReceiptLimitsQuery = "SELECT * FROM RECEIPT_LIMITS WHERE LIMITS_CONFIGURATION_ID = ?";

    @Override
    public LimitsConfiguration getConfiguration() {
        LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
        logger.info("------------------------");
        logger.info("Getting LimitsConfiguration");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getLimitsConfigurationQuery);
            while (resultSet.next()) {
                limitsConfiguration = getLimitsConfigurationFromResultSet(resultSet);
                loadReceiptLimits(limitsConfiguration, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("------------------------");
        logger.info("");
        return limitsConfiguration;
    }

    @Override
    public void updateConfiguration(LimitsConfiguration configuration) {
        logger.info("------------------------");
        logger.info("Updating LimitsConfiguration");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            updateConfiguration(configuration, connection);
            deleteOldReceiptLimits(connection);
            Map<String, Double> receiptLimits = configuration.getReceiptLimits();
            insertNewReceiptLimits(configuration, connection, receiptLimits);
            connection.commit();
            limitsConfiguration.setReceiptLimits(configuration.getReceiptLimits());
            limitsConfiguration.setDailyAllowanceRate(configuration.getDailyAllowanceRate());
            limitsConfiguration.setTotalReimbursementLimit(configuration.getTotalReimbursementLimit());
            limitsConfiguration.setCarMileageRate(configuration.getCarMileageRate());
            limitsConfiguration.setMileageLimitInKilometers(configuration.getMileageLimitInKilometers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("------------------------");
        logger.info("");
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

    public void insertConfiguration(LimitsConfiguration configuration) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int generatedId = 0;
            try (PreparedStatement statement = connection.prepareStatement(insertConfigurationQuery, Statement.RETURN_GENERATED_KEYS)) {
                statement.setDouble(1, configuration.getDailyAllowanceRate());
                statement.setDouble(2, configuration.getCarMileageRate());
                statement.setInt(3, configuration.getTotalReimbursementLimit());
                statement.setInt(4, configuration.getMileageLimitInKilometers());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        configuration.setId(generatedId);
                        insertNewReceiptLimits(configuration, connection, configuration.getReceiptLimits());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private void updateConfiguration(LimitsConfiguration configuration, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(updateConfigurationQuery)) {
            statement.setDouble(1, configuration.getDailyAllowanceRate());
            statement.setDouble(2, configuration.getCarMileageRate());
            statement.setInt(3, configuration.getTotalReimbursementLimit());
            statement.setInt(4, configuration.getMileageLimitInKilometers());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    configuration.setId(generatedId);
                    logger.info("configuration Id:" + generatedId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void deleteOldReceiptLimits(Connection connection) throws SQLException {
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteReceiptLimitsQuery)) {
            deleteStatement.executeUpdate();
        }
    }

    private void insertNewReceiptLimits(LimitsConfiguration configuration, Connection connection, Map<String, Double> receiptLimits) {
        try (PreparedStatement statement = connection.prepareStatement(insertReceiptLimitQuery)) {
            for (Map.Entry<String, Double> entry : receiptLimits.entrySet()) {
                statement.setString(1, entry.getKey());
                statement.setDouble(2, entry.getValue());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private LimitsConfiguration getLimitsConfigurationFromResultSet(ResultSet resultSet) throws SQLException {
        LimitsConfiguration limitsConfiguration = LimitsConfiguration.getInstance();
        limitsConfiguration.setId(resultSet.getInt("id"));
        limitsConfiguration.setDailyAllowanceRate(resultSet.getDouble("DAILY_ALLOWANCE_RATE"));
        limitsConfiguration.setCarMileageRate(resultSet.getDouble("CAR_MILEAGE_RATE"));
        limitsConfiguration.setTotalReimbursementLimit(resultSet.getInt("TOTAL_REIMBURSEMENT_LIMIT"));
        limitsConfiguration.setMileageLimitInKilometers(resultSet.getInt("MILEAGE_LIMIT_IN_KILOMETERS"));
        return limitsConfiguration;
    }


}
