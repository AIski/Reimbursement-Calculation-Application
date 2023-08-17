package pl.Alski.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.Alski.entity.claim.ReimbursementClaim;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "USER")
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "EMPLOYED_AT")
    private LocalDate employedAt;

    private List<ReimbursementClaim> claims;

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
