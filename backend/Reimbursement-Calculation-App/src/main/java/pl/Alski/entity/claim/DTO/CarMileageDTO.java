package pl.Alski.entity.claim.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class CarMileageDTO {
    private int distanceInKM;
    private String carPlates;
    private double reimbursementAmount;
}
