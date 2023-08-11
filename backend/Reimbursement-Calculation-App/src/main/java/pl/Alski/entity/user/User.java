package pl.Alski.entity.user;

import lombok.Data;
import pl.Alski.entity.claim.ReimbursementClaim;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
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

    @OneToMany(mappedBy = "user")
    private List<ReimbursementClaim> claims;
}
