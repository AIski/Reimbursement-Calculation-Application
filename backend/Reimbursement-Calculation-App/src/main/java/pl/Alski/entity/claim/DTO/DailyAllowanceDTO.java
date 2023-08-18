package pl.Alski.entity.claim.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class DailyAllowanceDTO {
    private ArrayList<LocalDate> reimbursedDays;
    private double reimbursementAmount;
}
