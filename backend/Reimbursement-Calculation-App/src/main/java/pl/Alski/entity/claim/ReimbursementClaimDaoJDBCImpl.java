package pl.Alski.entity.claim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.user.User;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbursementClaimDaoJDBCImpl implements ReimbursementClaimDao {
    private final Logger logger = LoggerFactory.getLogger(ReimbursementClaimDaoJDBCImpl.class);
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

    private final String getClaimsByUserIdQuery = "SELECT * FROM CLAIM " +
            "LEFT JOIN DAILY_ALLOWANCE ON CLAIM.ID = DAILY_ALLOWANCE.CLAIM_ID " +
            "LEFT JOIN CAR_MILEAGE ON CLAIM.ID = CAR_MILEAGE.CLAIM_ID " +
            "LEFT JOIN RECEIPT ON CLAIM.ID = RECEIPT.CLAIM_ID " +
            "WHERE CLAIM.USER_ID = ?";


    @Override
    public List<ReimbursementClaim> getClaimsByUserId(int userId) {
        logger.info("Getting claims by userID: " + userId);
        List<ReimbursementClaim> claims = new ArrayList<>();
        Map<Integer, ReimbursementClaim> claimMap = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(getClaimsByUserIdQuery);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                logger.info("Processing claims by userID: " + userId);
                ReimbursementClaim claim = processClaim(resultSet, claimMap);
                logger.info("Processing DailAllowance by userID: " + userId);
                processDailyAllowance(resultSet, claim);
                logger.info("Processing CarMileage by userID: " + userId);
                processCarMileage(resultSet, claim);
                logger.info("Processing Receipts by userID: " + userId);
                processReceipt(resultSet, claim);
                addUser(userId, claim);
            }
            claims.addAll(claimMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Returning claims " + claims.size());
        return claims;
    }


    private ReimbursementClaim processClaim(ResultSet resultSet, Map<Integer, ReimbursementClaim> claimMap) throws SQLException {
        int claimId = resultSet.getInt("CLAIM.ID");
        ReimbursementClaim claim = claimMap.get(claimId);
        if (claim == null) {
            claim = new ReimbursementClaim();
            claim.setId(claimId);
            claim.setStartDate(resultSet.getDate("START_DATE").toLocalDate());
            claim.setEndDate(resultSet.getDate("END_DATE").toLocalDate());
            claim.setStatus(ClaimStatus.valueOf(resultSet.getString("STATUS")));
            claim.setTotalReimbursementAmount(resultSet.getDouble("TOTAL_REIMBURSEMENT_AMOUNT"));
            claim.setReceipts(new ArrayList<>());
            claimMap.put(claimId, claim);
        }
        return claim;
    }

    private void processDailyAllowance(ResultSet resultSet, ReimbursementClaim claim) throws SQLException {
        if (resultSet.getInt("DAILY_ALLOWANCE.ID") != 0) {
            DailyAllowance dailyAllowance = new DailyAllowance();
            dailyAllowance.setId(resultSet.getInt("DAILY_ALLOWANCE.ID"));
            dailyAllowance.setReimbursementAmount(resultSet.getDouble("REIMBURSED_AMOUNT"));
            claim.setDailyAllowance(dailyAllowance);
        }
    }

    private void processCarMileage(ResultSet resultSet, ReimbursementClaim claim) throws SQLException {
        if (resultSet.getInt("CAR_MILEAGE.ID") != 0) {
            CarMileage carMileage = new CarMileage();
            carMileage.setId(resultSet.getInt("CAR_MILEAGE.ID"));
            carMileage.setDistanceInKM(resultSet.getInt("DISTANCE_IN_KM"));
            carMileage.setCarPlates(resultSet.getString("CAR_PLATES"));
            carMileage.setReimbursementAmount(resultSet.getDouble("CAR_MILEAGE.REIMBURSED_AMOUNT"));
            claim.setCarMileage(carMileage);
        }
    }

    private void processReceipt(ResultSet resultSet, ReimbursementClaim claim) throws SQLException {
        if (resultSet.getInt("RECEIPT.ID") != 0) {
            Receipt receipt = new Receipt();
            receipt.setId(resultSet.getInt("RECEIPT.ID"));
            receipt.setName(resultSet.getString("NAME"));
            receipt.setReimbursedAmount(resultSet.getDouble("RECEIPT.REIMBURSED_AMOUNT"));
            claim.getReceipts().add(receipt);
        }
    }
    private void addUser(int userId, ReimbursementClaim claim) {
        User user = new User();
        user.setId(userId);
        claim.setUser(user);
    }


    @Override
    public void saveClaim(ReimbursementClaim claim) {
        logger.info("Saving Claim ...");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);
            try (PreparedStatement claimStatement = connection.prepareStatement(insertClaimQuery, Statement.RETURN_GENERATED_KEYS)) {
                setClaimParameters(claim, claimStatement);
                int claimId = executeAndGetGeneratedKey(claimStatement);
                insertDailyAllowance(connection, claim.getDailyAllowance(), claimId);
                insertCarMileage(connection, claim.getCarMileage(), claimId);
                insertReceipts(connection, claim.getReceipts(), claimId);
                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setClaimParameters(ReimbursementClaim claim, PreparedStatement claimStatement) throws SQLException {
        claimStatement.setTimestamp(1, Timestamp.from(Instant.now()));
        claimStatement.setDate(2, Date.valueOf(claim.getStartDate()));
        claimStatement.setDate(3, Date.valueOf(claim.getEndDate()));
        claimStatement.setString(4, claim.getStatus().toString());
        claimStatement.setDouble(5, claim.getTotalReimbursementAmount());
        claimStatement.setInt(6, claim.getUser().getId());
    }


    private int executeAndGetGeneratedKey(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            throw new SQLException("Failed to retrieve generated key.");
        }
    }

    private void insertDailyAllowance(Connection connection, DailyAllowance dailyAllowance, int claimId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertDailyAllowanceQuery)) {
            statement.setDouble(1, dailyAllowance.getReimbursementAmount());
            statement.setInt(2, claimId);
            statement.executeUpdate();
        }
    }

    private void insertCarMileage(Connection connection, CarMileage carMileage, int claimId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(insertCarMileageQuery)) {
            statement.setInt(1, carMileage.getDistanceInKM());
            statement.setString(2, carMileage.getCarPlates());
            statement.setDouble(3, carMileage.getReimbursementAmount());
            statement.setInt(4, claimId);
            statement.executeUpdate();
        }
    }

    private void insertReceipts(Connection connection, List<Receipt> receipts, int claimId) throws SQLException {
        for (Receipt receipt : receipts) {
            try (PreparedStatement statement = connection.prepareStatement(insertReceiptQuery)) {
                statement.setString(1, receipt.getName());
                statement.setDouble(2, receipt.getReimbursedAmount());
                statement.setInt(3, claimId);
                statement.executeUpdate();
            }
        }
    }
}
