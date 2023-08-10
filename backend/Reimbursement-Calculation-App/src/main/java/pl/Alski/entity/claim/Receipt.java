package pl.Alski.entity.claim;

import lombok.Data;

@Data
public class Receipt {
    private int id;
    private String name;
    private double reimbursedAmount;
}
