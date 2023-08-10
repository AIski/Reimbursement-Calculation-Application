package pl.Alski.entity.claim;

import lombok.Data;

@Data
public class CarMileage {
    private int id;
    private int distanceInKilometers;
    private String carPlates;
    private double reimbursementAmount;
}
