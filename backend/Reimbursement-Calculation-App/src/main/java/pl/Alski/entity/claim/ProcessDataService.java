package pl.Alski.entity.claim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.claim.DTO.ClaimRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class ProcessDataService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessDataService.class);

    public void setClaimParameters(ClaimRequest claimRequest, PreparedStatement claimStatement) {
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
    }

    public ReimbursementClaim processClaim(ResultSet resultSet, Map<Integer, ReimbursementClaim> claimMap) {
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

    public void processCarMileage(ResultSet resultSet, ReimbursementClaim claim) {
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

    public int executeAndGetGeneratedKey(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }
    }
