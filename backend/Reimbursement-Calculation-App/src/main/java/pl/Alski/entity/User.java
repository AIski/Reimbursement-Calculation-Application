package pl.Alski.entity;

import java.time.LocalDate;
import java.util.List;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String companyName;
    private LocalDate employeedAt;
    private List<ReimbursementClaim> claims;
    private UserDetails userDetails;
}
