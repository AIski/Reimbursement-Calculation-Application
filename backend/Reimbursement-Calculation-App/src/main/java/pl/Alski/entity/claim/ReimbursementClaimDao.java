package pl.Alski.entity.claim;

import pl.Alski.entity.claim.DTO.ClaimRequest;

import java.util.List;

public interface ReimbursementClaimDao {

    public List<ReimbursementClaim> getClaimsByUserId(int userId);
    public void saveClaim(ClaimRequest claimRequest);
}
