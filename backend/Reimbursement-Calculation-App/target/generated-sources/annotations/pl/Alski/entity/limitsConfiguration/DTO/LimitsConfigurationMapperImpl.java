package pl.Alski.entity.limitsConfiguration.DTO;

import java.util.HashMap;
import javax.annotation.processing.Generated;
import pl.Alski.entity.limitsConfiguration.LimitsConfiguration;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T20:28:47+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
public class LimitsConfigurationMapperImpl implements LimitsConfigurationMapper {

    @Override
    public LimitsConfigurationDTO configurationToDto(LimitsConfiguration claim) {
        if ( claim == null ) {
            return null;
        }

        LimitsConfigurationDTO limitsConfigurationDTO = new LimitsConfigurationDTO();

        limitsConfigurationDTO.setDailyAllowanceRate( claim.getDailyAllowanceRate() );
        limitsConfigurationDTO.setCarMileageRate( claim.getCarMileageRate() );
        limitsConfigurationDTO.setTotalReimbursementLimit( claim.getTotalReimbursementLimit() );
        limitsConfigurationDTO.setMileageLimitInKilometers( claim.getMileageLimitInKilometers() );
        HashMap<String, Double> hashMap = claim.getReceiptLimits();
        if ( hashMap != null ) {
            limitsConfigurationDTO.setReceiptLimits( new HashMap<String, Double>( hashMap ) );
        }

        return limitsConfigurationDTO;
    }

    @Override
    public LimitsConfiguration dtoToClaim(LimitsConfigurationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LimitsConfiguration limitsConfiguration = new LimitsConfiguration();

        limitsConfiguration.setDailyAllowanceRate( dto.getDailyAllowanceRate() );
        limitsConfiguration.setCarMileageRate( dto.getCarMileageRate() );
        limitsConfiguration.setTotalReimbursementLimit( dto.getTotalReimbursementLimit() );
        limitsConfiguration.setMileageLimitInKilometers( dto.getMileageLimitInKilometers() );
        HashMap<String, Double> hashMap = dto.getReceiptLimits();
        if ( hashMap != null ) {
            limitsConfiguration.setReceiptLimits( new HashMap<String, Double>( hashMap ) );
        }

        return limitsConfiguration;
    }
}
