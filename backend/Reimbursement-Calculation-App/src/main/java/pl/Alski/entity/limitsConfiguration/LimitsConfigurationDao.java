package pl.Alski.entity.limitsConfiguration;

public interface LimitsConfigurationDao {
     LimitsConfiguration getConfiguration();
     void updateConfiguration(LimitsConfiguration configuration);
     void insertConfiguration(LimitsConfiguration configuration);
}
