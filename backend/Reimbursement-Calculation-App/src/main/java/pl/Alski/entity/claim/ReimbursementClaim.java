package pl.Alski.entity.claim;

import lombok.Data;
import pl.Alski.entity.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "CLAIM")
public class ReimbursementClaim {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @ManyToOne
    @JoinColumn(name = "ID")
    private ArrayList<Receipt> receipts;

    @OneToOne
    @JoinColumn(name = "DAILY_ALLOWANCE_ID", referencedColumnName = "ID")
    private DailyAllowance dailyAllowance;

    @OneToOne
    @JoinColumn(name = "CAR_MILEAGE_ID", referencedColumnName = "ID")
    private CarMileage carMileage;

    @Column(name = "TOTAL_REIMBURSEMENT_AMOUNT")
    private double totalReimbursementAmount;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
