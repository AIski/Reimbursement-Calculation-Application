package pl.Alski.entity.claim.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.Alski.entity.claim.CarMileage;
import pl.Alski.entity.claim.DailyAllowance;
import pl.Alski.entity.claim.Receipt;
import pl.Alski.entity.claim.ReimbursementClaim;
import pl.Alski.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-18T12:20:59+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
public class ClaimMapperImpl implements ClaimMapper {

    @Override
    public ClaimDTO claimToDto(ReimbursementClaim claim) {
        if ( claim == null ) {
            return null;
        }

        ClaimDTO claimDTO = new ClaimDTO();

        claimDTO.setUserId( claimUserId( claim ) );
        claimDTO.setDailyAllowance( dailyAllowanceToDailyAllowanceDTO( claim.getDailyAllowance() ) );
        claimDTO.setCarMileage( carMileageToCarMileageDTO( claim.getCarMileage() ) );
        claimDTO.setId( claim.getId() );
        claimDTO.setStartDate( claim.getStartDate() );
        claimDTO.setEndDate( claim.getEndDate() );
        claimDTO.setReceipts( receiptArrayListToReceiptDTOArrayList( claim.getReceipts() ) );
        claimDTO.setTotalReimbursementAmount( claim.getTotalReimbursementAmount() );

        return claimDTO;
    }

    @Override
    public ReimbursementClaim dtoToClaim(ClaimDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ReimbursementClaim reimbursementClaim = new ReimbursementClaim();

        reimbursementClaim.setUser( claimDTOToUser( dto ) );
        reimbursementClaim.setDailyAllowance( dailyAllowanceDTOToDailyAllowance( dto.getDailyAllowance() ) );
        reimbursementClaim.setCarMileage( carMileageDTOToCarMileage( dto.getCarMileage() ) );
        reimbursementClaim.setId( dto.getId() );
        reimbursementClaim.setStartDate( dto.getStartDate() );
        reimbursementClaim.setEndDate( dto.getEndDate() );
        reimbursementClaim.setReceipts( receiptDTOArrayListToReceiptArrayList( dto.getReceipts() ) );
        reimbursementClaim.setTotalReimbursementAmount( dto.getTotalReimbursementAmount() );

        return reimbursementClaim;
    }

    @Override
    public List<ClaimDTO> claimsToDTOs(List<ReimbursementClaim> claims) {
        if ( claims == null ) {
            return null;
        }

        List<ClaimDTO> list = new ArrayList<ClaimDTO>( claims.size() );
        for ( ReimbursementClaim reimbursementClaim : claims ) {
            list.add( claimToDto( reimbursementClaim ) );
        }

        return list;
    }

    @Override
    public List<ReimbursementClaim> DTOsToClaims(List<ClaimDTO> claimDTOS) {
        if ( claimDTOS == null ) {
            return null;
        }

        List<ReimbursementClaim> list = new ArrayList<ReimbursementClaim>( claimDTOS.size() );
        for ( ClaimDTO claimDTO : claimDTOS ) {
            list.add( dtoToClaim( claimDTO ) );
        }

        return list;
    }

    @Override
    public ReimbursementClaim claimRequestToDTO(ClaimRequest claimRequest) {
        if ( claimRequest == null ) {
            return null;
        }

        ReimbursementClaim reimbursementClaim = new ReimbursementClaim();

        reimbursementClaim.setStartDate( claimRequest.getStartDate() );
        reimbursementClaim.setEndDate( claimRequest.getEndDate() );
        reimbursementClaim.setReceipts( receiptDTOListToReceiptArrayList( claimRequest.getReceipts() ) );
        reimbursementClaim.setTotalReimbursementAmount( claimRequest.getTotalReimbursementAmount() );

        return reimbursementClaim;
    }

    private int claimUserId(ReimbursementClaim reimbursementClaim) {
        if ( reimbursementClaim == null ) {
            return 0;
        }
        User user = reimbursementClaim.getUser();
        if ( user == null ) {
            return 0;
        }
        int id = user.getId();
        return id;
    }

    protected DailyAllowanceDTO dailyAllowanceToDailyAllowanceDTO(DailyAllowance dailyAllowance) {
        if ( dailyAllowance == null ) {
            return null;
        }

        DailyAllowanceDTO dailyAllowanceDTO = new DailyAllowanceDTO();

        ArrayList<LocalDate> arrayList = dailyAllowance.getReimbursedDays();
        if ( arrayList != null ) {
            dailyAllowanceDTO.setReimbursedDays( new ArrayList<LocalDate>( arrayList ) );
        }
        dailyAllowanceDTO.setReimbursementAmount( dailyAllowance.getReimbursementAmount() );

        return dailyAllowanceDTO;
    }

    protected CarMileageDTO carMileageToCarMileageDTO(CarMileage carMileage) {
        if ( carMileage == null ) {
            return null;
        }

        CarMileageDTO carMileageDTO = new CarMileageDTO();

        carMileageDTO.setDistanceInKM( carMileage.getDistanceInKM() );
        carMileageDTO.setCarPlates( carMileage.getCarPlates() );
        carMileageDTO.setReimbursementAmount( carMileage.getReimbursementAmount() );

        return carMileageDTO;
    }

    protected ReceiptDTO receiptToReceiptDTO(Receipt receipt) {
        if ( receipt == null ) {
            return null;
        }

        ReceiptDTO receiptDTO = new ReceiptDTO();

        receiptDTO.setName( receipt.getName() );
        receiptDTO.setReimbursedAmount( receipt.getReimbursedAmount() );

        return receiptDTO;
    }

    protected ArrayList<ReceiptDTO> receiptArrayListToReceiptDTOArrayList(ArrayList<Receipt> arrayList) {
        if ( arrayList == null ) {
            return null;
        }

        ArrayList<ReceiptDTO> arrayList1 = new ArrayList<ReceiptDTO>();
        for ( Receipt receipt : arrayList ) {
            arrayList1.add( receiptToReceiptDTO( receipt ) );
        }

        return arrayList1;
    }

    protected User claimDTOToUser(ClaimDTO claimDTO) {
        if ( claimDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( claimDTO.getUserId() );

        return user;
    }

    protected DailyAllowance dailyAllowanceDTOToDailyAllowance(DailyAllowanceDTO dailyAllowanceDTO) {
        if ( dailyAllowanceDTO == null ) {
            return null;
        }

        DailyAllowance dailyAllowance = new DailyAllowance();

        ArrayList<LocalDate> arrayList = dailyAllowanceDTO.getReimbursedDays();
        if ( arrayList != null ) {
            dailyAllowance.setReimbursedDays( new ArrayList<LocalDate>( arrayList ) );
        }
        dailyAllowance.setReimbursementAmount( dailyAllowanceDTO.getReimbursementAmount() );

        return dailyAllowance;
    }

    protected CarMileage carMileageDTOToCarMileage(CarMileageDTO carMileageDTO) {
        if ( carMileageDTO == null ) {
            return null;
        }

        CarMileage carMileage = new CarMileage();

        carMileage.setDistanceInKM( carMileageDTO.getDistanceInKM() );
        carMileage.setCarPlates( carMileageDTO.getCarPlates() );
        carMileage.setReimbursementAmount( carMileageDTO.getReimbursementAmount() );

        return carMileage;
    }

    protected Receipt receiptDTOToReceipt(ReceiptDTO receiptDTO) {
        if ( receiptDTO == null ) {
            return null;
        }

        Receipt receipt = new Receipt();

        receipt.setName( receiptDTO.getName() );
        receipt.setReimbursedAmount( receiptDTO.getReimbursedAmount() );

        return receipt;
    }

    protected ArrayList<Receipt> receiptDTOArrayListToReceiptArrayList(ArrayList<ReceiptDTO> arrayList) {
        if ( arrayList == null ) {
            return null;
        }

        ArrayList<Receipt> arrayList1 = new ArrayList<Receipt>();
        for ( ReceiptDTO receiptDTO : arrayList ) {
            arrayList1.add( receiptDTOToReceipt( receiptDTO ) );
        }

        return arrayList1;
    }

    protected ArrayList<Receipt> receiptDTOListToReceiptArrayList(List<ReceiptDTO> list) {
        if ( list == null ) {
            return null;
        }

        ArrayList<Receipt> arrayList = new ArrayList<Receipt>();
        for ( ReceiptDTO receiptDTO : list ) {
            arrayList.add( receiptDTOToReceipt( receiptDTO ) );
        }

        return arrayList;
    }
}
