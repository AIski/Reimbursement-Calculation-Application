package pl.Alski.entity.limitsConfiguration.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitsConfigurationDTO {
    private double dailyAllowanceRate;
    private double carMileageRate;
    private int totalReimbursementLimit;
    private int mileageLimitInKilometers;
    private HashMap<String, Double> receiptLimits;
}
