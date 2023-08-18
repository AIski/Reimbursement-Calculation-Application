package pl.Alski.entity.claim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.claim.DTO.ClaimRequest;
import pl.Alski.entity.claim.DTO.ReceiptDTO;
import pl.Alski.entity.user.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.Alski.databaseInitializer.LimitsInitializer.limitsConfiguration;

public class ReimbursementClaimDaoJDBCImpl implements ReimbursementClaimDao {
    private static final Logger logger = LoggerFactory.getLogger(ReimbursementClaimDaoJDBCImpl.class);
    private final ProcessDataService processDataService= new ProcessDataService();
    private static final String DB_URL = "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    private final String insertClaimQuery = "INSERT INTO `CLAIM` " +
            "(START_DATE, END_DATE, STATUS, TOTAL_REIMBURSEMENT_AMOUNT, USER_ID) " +
            "VALUES (?, ?, ?, ?, ?)";

    private final String insertDailyAllowanceQuery = "INSERT INTO `DAILY_ALLOWANCE` " +
            "(REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES (?, ?)";

    private final String insertCarMileageQuery = "INSERT INTO `CAR_MILEAGE` " +
            "(DISTANCE_IN_KM, CAR_PLATES, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES (?, ?, ?, ?)";

    private final String insertReceiptQuery = "INSERT INTO `RECEIPT` " +
            "(NAME, REIMBURSED_AMOUNT, CLAIM_ID) " +
            "VALUES (?, ?, ?)";

    private final String insertReimbursedDaysQuery = "INSERT INTO `REIMBURSED_DAYS` " +
            "(ALLOWANCE_ID, REIMBURSED_DAY) " +
            "VALUES (?, ?)";

    private final String getClaimsAndRelatedDataQuery = "SELECT * FROM CLAIM  " +
            "LEFT JOIN CAR_MILEAGE ON CLAIM.ID = CAR_MILEAGE.CLAIM_ID " +
            "LEFT JOIN DAILY_ALLOWANCE ON CLAIM.ID = DAILY_ALLOWANCE.CLAIM_ID " +
            "WHERE CLAIM.USER_ID = ?";

    private final String getReimbursedDaysByAllowanceIdQuery = "SELECT DAILY_ALLOWANCE.ID, REIMBURSED_DAYS.REIMBURSED_DAY " +
            "FROM DAILY_ALLOWANCE " +
            "LEFT JOIN REIMBURSED_DAYS ON DAILY_ALLOWANCE.ID = REIMBURSED_DAYS.ALLOWANCE_ID " +
            "WHERE DAILY_ALLOWANCE.ID = ?";
    private final String getReceiptsByClaimIdsQuery = "SELECT CLAIM.ID AS CLAIM_ID, RECEIPT.* " +
            "FROM CLAIM " +
            "LEFT JOIN RECEIPT ON CLAIM.ID = RECEIPT.CLAIM_ID " +
            "WHERE CLAIM.USER_ID = ?";

    @Override
    public List<ReimbursementClaim> getClaimsByUserId(int userId) {
        logger.info("------------------------");
        logger.info("Getting claims by userID: " + userId);
        List<ReimbursementClaim> claims = new ArrayList<>();
        Map<Integer, ReimbursementClaim> claimMap = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(getClaimsAndRelatedDataQuery);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ReimbursementClaim claim = processDataService.processClaim(resultSet, claimMap);
                logger.info("processing CarMileage...");
                processDataService.processCarMileage(resultSet, claim);
                logger.info("processing DailyAllowance...");
                processDailyAllowance(resultSet, claim);
                addUser(userId, claim);
                logger.info(claim.toString());
            }
            logger.info("Fetching and processing Receipts...");
            fetchAndSetReceipts(userId, claimMap);
            claims.addAll(claimMap.values());
            logger.info("------------------------");
            logger.info("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    private void processDailyAllowance(ResultSet resultSet, ReimbursementClaim claim) {
        try {
            if (resultSet.getInt("DAILY_ALLOWANCE.ID") != 0) {
                DailyAllowance dailyAllowance = new DailyAllowance();
                dailyAllowance.setId(resultSet.getInt("DAILY_ALLOWANCE.ID"));
                dailyAllowance.setReimbursementAmount(resultSet.getDouble("REIMBURSED_AMOUNT"));
                claim.setDailyAllowance(dailyAllowance);

                int dailyAllowanceId = dailyAllowance.getId();
                fetchAndSetReimbursedDays(dailyAllowance, dailyAllowanceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void fetchAndSetReimbursedDays(DailyAllowance dailyAllowance, int dailyAllowanceId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(getReimbursedDaysByAllowanceIdQuery);
            statement.setInt(1, dailyAllowanceId);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<LocalDate> reimbursedDays = new ArrayList<>();
            while (resultSet.next()) {
                LocalDate reimbursedDay = resultSet.getDate("REIMBURSED_DAY").toLocalDate();
                reimbursedDays.add(reimbursedDay);
            }

            dailyAllowance.setReimbursedDays(reimbursedDays);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void fetchAndSetReceipts(int userId, Map<Integer, ReimbursementClaim> claimMap) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(getReceiptsByClaimIdsQuery);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int claimId = resultSet.getInt("CLAIM_ID");
                ReimbursementClaim claim = claimMap.get(claimId);
                if (claim != null) {
                    Receipt receipt = new Receipt();
                    receipt.setId(resultSet.getInt("ID"));
                    receipt.setName(resultSet.getString("NAME"));
                    receipt.setReimbursedAmount(resultSet.getDouble("REIMBURSED_AMOUNT"));
                    claim.getReceipts().add(receipt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void addUser(int userId, ReimbursementClaim claim) {
        User user = new User();
        user.setId(userId);
        claim.setUser(user);
    }

    @Override
    public void saveClaim(ClaimRequest claimRequest) {
        logger.info("------------------------");
        logger.info("Saving Claim ...");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);
            try (PreparedStatement claimStatement = connection.prepareStatement(insertClaimQuery, Statement.RETURN_GENERATED_KEYS)) {
                processDataService.setClaimParameters(claimRequest, claimStatement);
                int claimId = processDataService.executeAndGetGeneratedKey(claimStatement);
                if (claimRequest.getReimbursedDaysWithAllowance() != null) {
                    logger.info("inserting Daily Allowance ...");
                    int allowanceId = insertDailyAllowance(connection, claimRequest, claimId);
                    logger.info("Inserting Reimbursed Days ...");
                    insertReimbursedDays(connection, claimRequest.getReimbursedDaysWithAllowance(), allowanceId);
                }
                if (claimRequest.getDistanceInKMForCarMileage() != 0) {
                    logger.info("inserting Car Mileage ...");
                    insertCarMileage(connection, claimRequest, claimId);
                }
                if (claimRequest.getReceipts() != null) {
                    logger.info("inserting Receipts ...");
                    insertReceipts(connection, claimRequest.getReceipts(), claimId);
                }
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        logger.info("------------------------");
        logger.info("");

    }

    private int insertDailyAllowance(Connection connection, ClaimRequest claimRequest, int claimId) {
        try (PreparedStatement statement = connection.prepareStatement(insertDailyAllowanceQuery, Statement.RETURN_GENERATED_KEYS)) {
            double allowanceRate = limitsConfiguration.getDailyAllowanceRate();
            logger.info("Allowance Rate: " + allowanceRate);
            double reimbursementAmount = allowanceRate * claimRequest.getReimbursedDaysWithAllowance().size();
            logger.info("ReimbursementAmount:  " + reimbursementAmount);
            statement.setDouble(1, reimbursementAmount);
            statement.setInt(2, claimId);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private void insertReimbursedDays(Connection connection, List<LocalDate> reimbursedDays, int allowanceId) {
        try {
            try (PreparedStatement reimbursedDaysStatement = connection.prepareStatement(insertReimbursedDaysQuery)) {
                for (LocalDate reimbursedDay : reimbursedDays) {
                    reimbursedDaysStatement.setInt(1, allowanceId);
                    reimbursedDaysStatement.setDate(2, Date.valueOf(reimbursedDay));
                    reimbursedDaysStatement.addBatch();
                }
                reimbursedDaysStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertCarMileage(Connection connection, ClaimRequest claimRequest, int claimId) {
        try (PreparedStatement statement = connection.prepareStatement(insertCarMileageQuery)) {
            statement.setInt(1, claimRequest.getDistanceInKMForCarMileage());
            statement.setString(2, claimRequest.getCarPlates());
            logger.info("carMileageRate: " + limitsConfiguration.getCarMileageRate());
            var reimbursementAmount = limitsConfiguration.getCarMileageRate() * claimRequest.getDistanceInKMForCarMileage();
            statement.setDouble(3, reimbursementAmount);
            statement.setInt(4, claimId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertReceipts(Connection connection, List<ReceiptDTO> receipts, int claimId) {
        for (ReceiptDTO receipt : receipts) {
            try (PreparedStatement statement = connection.prepareStatement(insertReceiptQuery)) {
                statement.setString(1, receipt.getName());
                statement.setDouble(2, receipt.getReimbursedAmount());
                statement.setInt(3, claimId);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
