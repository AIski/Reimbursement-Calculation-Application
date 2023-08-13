package pl.Alski.DAO;

import pl.Alski.entity.claim.ReimbursementClaim;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ReimbursementClaimDaoJpaImpl implements ReimbursementClaimDao{

    private EntityManager entityManager;

    public ReimbursementClaimDaoJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ReimbursementClaim> getClaims() {
        TypedQuery<ReimbursementClaim> theQuery = entityManager.createQuery("from CLAIM", ReimbursementClaim.class);
        return theQuery.getResultList();
    }

    @Override
    public ReimbursementClaim getClaimById(int id) {
        return entityManager.find(ReimbursementClaim.class, id);
    }

    @Override
    public void saveClaim(ReimbursementClaim claim) {
        ReimbursementClaim dbClaim = entityManager.merge(claim);
        claim.setId(claim.getId());
    }

    @Override
    public void deleteById(int id) {
        Query theQuery = entityManager.createQuery("delete from CLAIM where ID=:CLAIM_ID");
        theQuery.setParameter("CLAIM_ID", id);
        theQuery.executeUpdate();
    }
}
