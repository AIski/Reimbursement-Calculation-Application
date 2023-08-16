package pl.Alski.entity.limitsConfiguration;

import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;

public interface LimitsConfigurationDao {
    public LimitsConfiguration getConfiguration();
    public void saveConfiguration(LimitsConfiguration configuration);
}
