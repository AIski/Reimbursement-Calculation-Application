package pl.Alski.entity.claim.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.Alski.entity.claim.ReimbursementClaim;

import java.util.List;

@Mapper
public interface ClaimMapper {
    ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "dailyAllowance", source = "dailyAllowance")
    @Mapping(target = "carMileage", source = "carMileage")
    ClaimDTO claimToDto(ReimbursementClaim claim);
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "dailyAllowance", source = "dailyAllowance")
    @Mapping(target = "carMileage", source = "carMileage")
    ReimbursementClaim dtoToClaim(ClaimDTO dto);
    List<ClaimDTO> claimsToDTOs(List<ReimbursementClaim> claims);
    List<ReimbursementClaim> DTOsToClaims(List<ClaimDTO> claimDTOS);

    ReimbursementClaim claimRequestToDTO(ClaimRequest claimRequest);

}
