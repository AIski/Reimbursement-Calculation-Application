package pl.Alski.entity.user;

import lombok.Data;
import pl.Alski.entity.claim.ReimbursementClaim;

import java.time.LocalDate;
import java.util.List;

@Data
public class User {
    private int id;

    private String firstName;
    private String lastName;
    private String companyName;
    private LocalDate employedAt;
    private List<ReimbursementClaim> claims;
    private LimitsConfiguration limitsConfiguration;
}
