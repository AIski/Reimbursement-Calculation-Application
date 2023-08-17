package pl.Alski.entity.claim.DTO;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import pl.Alski.entity.claim.Receipt;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-17T20:28:48+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 11.0.16.1 (Amazon.com Inc.)"
)
public class ReceiptMapperImpl implements ReceiptMapper {

    @Override
    public ReceiptDTO receiptToDto(Receipt receipt) {
        if ( receipt == null ) {
            return null;
        }

        ReceiptDTO receiptDTO = new ReceiptDTO();

        receiptDTO.setName( receipt.getName() );
        receiptDTO.setReimbursedAmount( receipt.getReimbursedAmount() );

        return receiptDTO;
    }

    @Override
    public Receipt dtoToReceipt(ReceiptDTO receiptDto) {
        if ( receiptDto == null ) {
            return null;
        }

        Receipt receipt = new Receipt();

        receipt.setName( receiptDto.getName() );
        receipt.setReimbursedAmount( receiptDto.getReimbursedAmount() );

        return receipt;
    }

    @Override
    public List<ReceiptDTO> receiptsToDTOs(List<Receipt> receipts) {
        if ( receipts == null ) {
            return null;
        }

        List<ReceiptDTO> list = new ArrayList<ReceiptDTO>( receipts.size() );
        for ( Receipt receipt : receipts ) {
            list.add( receiptToDto( receipt ) );
        }

        return list;
    }

    @Override
    public List<Receipt> DTOsToReceipts(List<ReceiptDTO> receiptsDTOS) {
        if ( receiptsDTOS == null ) {
            return null;
        }

        List<Receipt> list = new ArrayList<Receipt>( receiptsDTOS.size() );
        for ( ReceiptDTO receiptDTO : receiptsDTOS ) {
            list.add( dtoToReceipt( receiptDTO ) );
        }

        return list;
    }
}
