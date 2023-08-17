package pl.Alski.entity.claim.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.Alski.entity.claim.Receipt;

import java.util.List;

@Mapper
public interface ReceiptMapper {
    ReceiptMapper INSTANCE = Mappers.getMapper(ReceiptMapper.class);

    ReceiptDTO receiptToDto(Receipt receipt);
    Receipt dtoToReceipt(ReceiptDTO receiptDto);
    List<ReceiptDTO> receiptsToDTOs(List<Receipt> receipts);
    List<Receipt> DTOsToReceipts(List<ReceiptDTO> receiptsDTOS);

}
