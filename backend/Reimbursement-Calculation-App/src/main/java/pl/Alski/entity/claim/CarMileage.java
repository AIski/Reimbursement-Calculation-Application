package pl.Alski.entity.claim;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CAR_MILEAGE")
public class CarMileage {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "DISTANCE_IN_KM")
    private int distanceInKM;

    @Column(name = "CAR_PLATES")
    private String carPlates;

    @Column(name = "REIMBURSED_AMOUNT")
    private double reimbursementAmount;
}
