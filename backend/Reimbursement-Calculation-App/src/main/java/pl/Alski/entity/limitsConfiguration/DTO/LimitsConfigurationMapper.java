package pl.Alski.entity.limitsConfiguration.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;

@Mapper
public interface LimitsConfigurationMapper {
    LimitsConfigurationMapper INSTANCE = Mappers.getMapper(LimitsConfigurationMapper.class);

    LimitsConfigurationDTO configurationToDto(LimitsConfiguration claim);
    LimitsConfiguration dtoToClaim(LimitsConfigurationDTO dto);
}
