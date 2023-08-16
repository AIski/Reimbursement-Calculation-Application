package pl.Alski.entity.claim;

import java.util.List;

public interface ReimbursementClaimDao {

    public List<ReimbursementClaim> getClaimsByUserId(int userId);
    public void saveClaim(ReimbursementClaim claim);
}
