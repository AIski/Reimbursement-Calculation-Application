package pl.Alski.entity.claim;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class DailyAllowance {
    private int id;
    private ArrayList<LocalDate> reimbursedDays;
    private double reimbursementAmount;
}
