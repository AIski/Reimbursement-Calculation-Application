package pl.Alski.DAO;

import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;

public interface LimitsConfigurationDAO {
    public LimitsConfiguration getConfiguration();
    public void saveConfiguration(LimitsConfiguration configuration);
}
