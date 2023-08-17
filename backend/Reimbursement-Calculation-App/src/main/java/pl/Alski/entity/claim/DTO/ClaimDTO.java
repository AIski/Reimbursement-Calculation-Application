package pl.Alski.entity.claim.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.Alski.entity.claim.CarMileage;
import pl.Alski.entity.claim.DailyAllowance;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ClaimDTO {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<ReceiptDTO> receipts;
    private DailyAllowance dailyAllowance;
    private CarMileage carMileage;
    private double totalReimbursementAmount;
    private int UserId;
}


