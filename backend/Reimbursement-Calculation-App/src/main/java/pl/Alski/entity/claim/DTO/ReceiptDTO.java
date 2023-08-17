package pl.Alski.entity.claim.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {
    private String name;
    private double reimbursedAmount;
}
