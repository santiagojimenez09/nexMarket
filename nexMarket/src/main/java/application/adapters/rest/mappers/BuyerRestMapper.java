package application.adapters.rest.mappers;

import application.adapters.rest.dtos.requests.RegisterBuyerRequestDTO;
import application.adapters.rest.dtos.responses.BuyerResponseDTO;
import application.domain.models.Buyer;

public class BuyerRestMapper {

    public static Buyer toDomain(RegisterBuyerRequestDTO dto) {
        if (dto == null) return null;
        Buyer buyer = new Buyer();
        buyer.setIdentifier(dto.getIdentifier());
        buyer.setFullName(dto.getName());
        buyer.setEmail(dto.getEmail());
        buyer.setPassword(dto.getPassword());
        buyer.setPrimaryAddress(dto.getPrimaryAddress());
        return buyer;
    }

    public static BuyerResponseDTO toResponseDTO(Buyer buyer) {
        if (buyer == null) return null;
        return BuyerResponseDTO.builder()
                .identifier(buyer.getIdentifier())
                .name(buyer.getFullName())
                .email(buyer.getEmail())
                .role(buyer.getRole() != null ? buyer.getRole().name() : null)
                .status(buyer.getStatus() != null ? buyer.getStatus().name() : null)
                .commercialStatus(buyer.getCommercialStatus() != null ? buyer.getCommercialStatus().name() : null)
                .primaryAddress(buyer.getPrimaryAddress())
                .additionalAddresses(buyer.getAdditionalAddresses())
                .build();
    }
}
