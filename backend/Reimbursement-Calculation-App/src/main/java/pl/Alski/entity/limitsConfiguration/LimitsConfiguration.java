package pl.Alski.entity.limitsConfiguration;

import javax.persistence.*;

import lombok.Data;

import java.util.HashMap;

@Data
@Entity
@Table(name = "LIMITS_CONFIGURATION")
public final class LimitsConfiguration {

    @Transient
    private static LimitsConfiguration INSTANCE;

    private LimitsConfiguration() {
    }

    public synchronized static LimitsConfiguration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LimitsConfiguration();
        }
        return INSTANCE;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DAILY_ALLOWANCE_RATE")
    private double dailyAllowanceRate;

    @Column(name = "CAR_MILEAGE_RATE")
    private double carMileageRate;

    @Column(name = "TOTAL_REIMBURSEMENT_LIMIT")
    private int totalReimbursementLimit;

    @Column(name = "MILEAGE_LIMIT_IN_KILOMETERS")
    private int mileageLimitInKilometers;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "RECEIPT_LIMITS",
            joinColumns = @JoinColumn(name = "LIMITS_CONFIGURATION_ID"))
    @Column(name = "PER_RECEIPT_LIMIT", nullable = false)
    @MapKeyColumn(name = "RECEIPT_TYPE")
    private HashMap<String, Double> receiptLimits;
}
