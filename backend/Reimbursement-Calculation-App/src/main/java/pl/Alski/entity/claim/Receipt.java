package pl.Alski.entity.claim;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "RECEIPT")
public class Receipt {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "REIMBURSED_AMOUNT")
    private double reimbursedAmount;
}
