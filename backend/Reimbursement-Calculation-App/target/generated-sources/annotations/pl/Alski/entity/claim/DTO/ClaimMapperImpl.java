package pl.Alski.entity.claim.DTO;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.Alski.entity.claim.Receipt;
import pl.Alski.entity.claim.ReimbursementClaim;
import pl.Alski.entity.user.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T20:28:48+0200",
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
        claimDTO.setId( claim.getId() );
        claimDTO.setStartDate( claim.getStartDate() );
        claimDTO.setEndDate( claim.getEndDate() );
        claimDTO.setReceipts( receiptArrayListToReceiptDTOArrayList( claim.getReceipts() ) );
        claimDTO.setDailyAllowance( claim.getDailyAllowance() );
        claimDTO.setCarMileage( claim.getCarMileage() );
        claimDTO.setTotalReimbursementAmount( claim.getTotalReimbursementAmount() );

        return claimDTO;
    }

    @Override
    public ReimbursementClaim dtoToClaim(ClaimDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ReimbursementClaim reimbursementClaim = new ReimbursementClaim();

        reimbursementClaim.setId( dto.getId() );
        reimbursementClaim.setStartDate( dto.getStartDate() );
        reimbursementClaim.setEndDate( dto.getEndDate() );
        reimbursementClaim.setReceipts( receiptDTOArrayListToReceiptArrayList( dto.getReceipts() ) );
        reimbursementClaim.setDailyAllowance( dto.getDailyAllowance() );
        reimbursementClaim.setCarMileage( dto.getCarMileage() );
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
