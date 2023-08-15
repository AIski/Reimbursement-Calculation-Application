package pl.Alski.entity.claim;

import java.util.List;

public class ReimbursementClaimDaoJDBCImpl implements ReimbursementClaimDao{
    @Override
    public List<ReimbursementClaim> getClaimsByUserId(int userId) {
        //create long query with multiple join, for all the sub-components (daily, car and receipts)
    }

    @Override
    public void saveClaim(ReimbursementClaim claim) {
       //save claim without sub-components

        //save daily allowance if exists (with claim id)

        //save car mileage if exists(with claim id)

        //save receipts if exists(with claim id)

    }
}
