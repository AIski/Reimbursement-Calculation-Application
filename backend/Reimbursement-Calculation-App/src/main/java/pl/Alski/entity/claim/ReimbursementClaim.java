package pl.Alski.entity.claim;

import lombok.Data;
import pl.Alski.entity.claim.ClaimStatus;
import pl.Alski.entity.claim.DailyAllowance;
import pl.Alski.entity.claim.Receipt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class ReimbursementClaim {
    private int id;

    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;

    private ClaimStatus status;

    private ArrayList<Receipt> receipts;
    private DailyAllowance dailyAllowance;
    private CarMileage carMileage;

    private int totalReimbursement;
}
