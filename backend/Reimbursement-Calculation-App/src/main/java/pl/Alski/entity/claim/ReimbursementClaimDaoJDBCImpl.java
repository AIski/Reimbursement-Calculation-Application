package pl.Alski.entity.claim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.claim.DTO.ClaimRequest;
import pl.Alski.entity.claim.DTO.ReceiptDTO;
import pl.Alski.entity.user.User;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static pl.Alski.databaseInitializer.LimitsInitializer.limitsConfiguration;

public class ReimbursementClaimDaoJDBCImpl implements ReimbursementClaimDao {
    private static final Logger logger = LoggerFactory.getLogger(ReimbursementClaimDaoJDBCImpl.class);
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

    private final String getClaimsByUserIdQuery = "SELECT * FROM CLAIM " +
            "LEFT JOIN CAR_MILEAGE ON CLAIM.ID = CAR_MILEAGE.CLAIM_ID " +
            "LEFT JOIN RECEIPT ON CLAIM.ID = RECEIPT.CLAIM_ID " +
            "LEFT JOIN DAILY_ALLOWANCE ON CLAIM.ID = DAILY_ALLOWANCE.CLAIM_ID " +
            "LEFT JOIN REIMBURSED_DAYS ON DAILY_ALLOWANCE.ID = REIMBURSED_DAYS.ALLOWANCE_ID " +
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
                ReimbursementClaim claim = processClaim(resultSet, claimMap);
                logger.info("processing CarMileage...");
                processCarMileage(resultSet, claim);
                logger.info("processing DailyAllowance...");
                processDailyAllowance(resultSet, claim);
                logger.info("processing Receipts...");

                // Add separate query for receipts... Its going to be easier, than this complex select statement.



                processReceipts(resultSet, claim);
                addUser(userId, claim);
                logger.info(claim.toString());
            }
            claims.addAll(claimMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info("Returning claims:" + claims.size());
        return claims;
    }


    private ReimbursementClaim processClaim(ResultSet resultSet, Map<Integer, ReimbursementClaim> claimMap) {
        int claimId = 0;
        try {
            claimId = resultSet.getInt("CLAIM.ID");
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
                return claim;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private void processDailyAllowance(ResultSet resultSet, ReimbursementClaim claim) {
        try {
            if (resultSet.getInt("DAILY_ALLOWANCE.ID") != 0) {
                DailyAllowance dailyAllowance = new DailyAllowance();
                dailyAllowance.setId(resultSet.getInt("DAILY_ALLOWANCE.ID"));
                dailyAllowance.setReimbursementAmount(resultSet.getDouble("REIMBURSED_AMOUNT"));
                claim.setDailyAllowance(dailyAllowance);

                processReimbursedDays(resultSet, claim.getDailyAllowance());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void processReimbursedDays(ResultSet resultSet, DailyAllowance dailyAllowance) {
        HashSet<LocalDate> reimbursedDays = new HashSet<>();
        try {
            while (resultSet.next()) {
                LocalDate reimbursedDay = resultSet.getDate("REIMBURSED_DAYS.REIMBURSED_DAY").toLocalDate();
                reimbursedDays.add(reimbursedDay);
            }
            ArrayList<LocalDate> reimbursedDaysList = new ArrayList<>(reimbursedDays);
            dailyAllowance.setReimbursedDays(reimbursedDaysList);
            logger.info(dailyAllowance.getReimbursedDays().toString());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void processCarMileage(ResultSet resultSet, ReimbursementClaim claim) {
        try {
            if (resultSet.getInt("CAR_MILEAGE.ID") != 0) {
                CarMileage carMileage = new CarMileage();
                carMileage.setId(resultSet.getInt("CAR_MILEAGE.ID"));
                carMileage.setDistanceInKM(resultSet.getInt("DISTANCE_IN_KM"));
                carMileage.setCarPlates(resultSet.getString("CAR_PLATES"));
                carMileage.setReimbursementAmount(resultSet.getDouble("CAR_MILEAGE.REIMBURSED_AMOUNT"));
                claim.setCarMileage(carMileage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void processReceipts(ResultSet resultSet, ReimbursementClaim claim) {
        try {
            while (resultSet.next()) {
                if (resultSet.getInt("RECEIPT.ID") != 0) {
                    Receipt receipt = new Receipt();
                    receipt.setId(resultSet.getInt("RECEIPT.ID"));
                    receipt.setName(resultSet.getString("NAME"));
                    receipt.setReimbursedAmount(resultSet.getDouble("RECEIPT.REIMBURSED_AMOUNT"));
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
                logger.info("Setting claim parameters");
                setClaimParameters(claimRequest, claimStatement);
                logger.info("executingClaimAndGeneratingKey ...");
                int claimId = executeAndGetGeneratedKey(claimStatement);

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
                logger.info("Saving claim...");
                connection.commit();
                logger.info("Finished saving claim.");
                logger.info("------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setClaimParameters(ClaimRequest claimRequest, PreparedStatement claimStatement) {
        try {
            claimStatement.setDate(1, Date.valueOf(claimRequest.getStartDate()));
            claimStatement.setDate(2, Date.valueOf(claimRequest.getEndDate()));
            claimStatement.setString(3, ClaimStatus.PENDING.toString());
            claimStatement.setDouble(4, claimRequest.getTotalReimbursementAmount());
            claimStatement.setInt(5, claimRequest.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        logger.info(claimStatement.toString());
    }

    private int executeAndGetGeneratedKey(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    logger.info("Claim id: " + String.valueOf(generatedKeys.getInt(1)));
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int insertDailyAllowance(Connection connection, ClaimRequest claimRequest, int claimId) {
        try (PreparedStatement statement = connection.prepareStatement(insertDailyAllowanceQuery, Statement.RETURN_GENERATED_KEYS)) {
            double allowanceRate = limitsConfiguration.getDailyAllowanceRate();
            logger.info("Allowance Rate: " + allowanceRate);
            logger.info("Days: " + claimRequest.getReimbursedDaysWithAllowance().size());
            double reimbursementAmount = allowanceRate * claimRequest.getReimbursedDaysWithAllowance().size();
            logger.info("ReimbursementAmount:  " + reimbursementAmount);
            statement.setDouble(1, reimbursementAmount);
            statement.setInt(2, claimId);
            logger.info(statement.toString());
            statement.executeUpdate();
            logger.info("executed update.");
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    logger.info("Allowance id: " + String.valueOf(generatedKeys.getInt(1)));
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
            var reimbursementAmount = limitsConfiguration.getCarMileageRate() * claimRequest.getDistanceInKMForCarMileage();
            logger.info("Reimbursed car mileage amount:"+reimbursementAmount);
            statement.setDouble(3, reimbursementAmount);
            statement.setInt(4, claimId);
            logger.info(statement.toString());
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
                logger.info(statement.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
