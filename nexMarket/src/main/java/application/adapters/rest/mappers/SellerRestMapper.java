package application.adapters.rest.mappers;

import application.adapters.rest.dtos.requests.RegisterSellerRequestDTO;
import application.adapters.rest.dtos.responses.SellerResponseDTO;
import application.domain.models.Seller;

public class SellerRestMapper {

    public static Seller toDomain(RegisterSellerRequestDTO dto) {
        if (dto == null) return null;
        Seller seller = new Seller();
        seller.setIdentifier(dto.getIdentifier());
        seller.setFullName(dto.getName());
        seller.setEmail(dto.getEmail());
        return seller;
    }

    public static SellerResponseDTO toResponseDTO(Seller seller) {
        if (seller == null) return null;
        return SellerResponseDTO.builder()
                .identifier(seller.getIdentifier())
                .name(seller.getFullName())
                .email(seller.getEmail())
                .status(seller.getStatus() != null ? seller.getStatus().name() : null)
                .build();
    }
}
