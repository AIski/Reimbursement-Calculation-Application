package pl.Alski.entity.claim;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "DAILY_ALLOWANCE")
public class DailyAllowance {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection(targetClass = LocalDate.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "REIMBURSED_DAYS", joinColumns = @JoinColumn(name = "ALLOWANCE_ID"))
    @Column(name = "REIMBURSED_DAYS", nullable = false)
    private ArrayList<LocalDate> reimbursedDays;

    @Column(name = "REIMBURSED_AMOUNT")
    private double reimbursementAmount;
}
