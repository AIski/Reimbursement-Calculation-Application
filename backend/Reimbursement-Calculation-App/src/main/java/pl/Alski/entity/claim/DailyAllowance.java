package pl.Alski.entity.claim;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import pl.Alski.LocalDateDeserializer;

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
    @CollectionTable(name = "REIMBURSED_DAYS", joinColumns = @JoinColumn(name = "DAY_ID"))
    @Column(name = "REIMBURSED_DAYS", nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ArrayList<LocalDate> reimbursedDays;

    @Column(name = "REIMBURSED_AMOUNT")
    private double reimbursementAmount;
}
