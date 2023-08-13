package pl.Alski.DAO;

import pl.Alski.entity.claim.ReimbursementClaim;

import java.util.List;

public interface ReimbursementClaimDao {

    public List<ReimbursementClaim> getClaims();
    public ReimbursementClaim getClaimById(int id);
    public void saveClaim(ReimbursementClaim claim);
    public void deleteById(int id);
}
