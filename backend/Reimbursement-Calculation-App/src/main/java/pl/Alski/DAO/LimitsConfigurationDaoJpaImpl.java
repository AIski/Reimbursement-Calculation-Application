package pl.Alski.DAO;

import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class LimitsConfigurationDaoJpaImpl implements LimitsConfigurationDao {

    private EntityManager entityManager;
    public LimitsConfigurationDaoJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public LimitsConfiguration getConfiguration() {
        TypedQuery<LimitsConfiguration> theQuery = entityManager.createQuery("from LIMITS_CONFIGURATION", LimitsConfiguration.class);
        return theQuery.getResultList().get(0);
    }

    @Override
    public void saveConfiguration(LimitsConfiguration configuration) {
        LimitsConfiguration dbConfig = entityManager.merge(configuration);
        configuration.setId(dbConfig.getId());
    }
}
