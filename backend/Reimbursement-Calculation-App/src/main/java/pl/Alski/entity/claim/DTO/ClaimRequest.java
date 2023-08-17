package pl.Alski.entity.claim.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ClaimRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ReceiptDTO> receipts;
    private List<LocalDate> reimbursedDaysWithAllowance;
    private int distanceInKMForCarMileage;
    private String carPlates;
    private double totalReimbursementAmount;
    private int userId;
}
