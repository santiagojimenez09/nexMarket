package application.adapters.rest.mappers;

import application.adapters.rest.dtos.requests.RegisterProductRequestDTO;
import application.adapters.rest.dtos.responses.ProductResponseDTO;
import application.domain.models.DigitalProduct;
import application.domain.models.PhysicalProduct;
import application.domain.models.Product;
import application.domain.valueObjects.ProductType;

import java.math.BigDecimal;

public class ProductRestMapper {

    public static Product toDomain(RegisterProductRequestDTO dto) {
        if (dto == null) return null;
        Product product;
        if ("DIGITAL".equalsIgnoreCase(dto.getProductType())) {
            DigitalProduct digital = new DigitalProduct();
            digital.setDeliveryAsset(dto.getDeliveryAsset());
            product = digital;
            product.setProductType(ProductType.DIGITAL);
        } else {
            PhysicalProduct physical = new PhysicalProduct();
            if (dto.getWeight() != null) {
                physical.setWeight(BigDecimal.valueOf(dto.getWeight()));
            }
            product = physical;
            product.setProductType(ProductType.PHYSICAL);
        }
        product.setIdentifier(dto.getIdentifier());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        return product;
    }

    public static ProductResponseDTO toResponseDTO(Product product) {
        if (product == null) return null;
        return ProductResponseDTO.builder()
                .identifier(product.getIdentifier())
                .name(product.getName())
                .description(product.getDescription())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .productType(product.getProductType() != null ? product.getProductType().name() : null)
                .sellerIdentifier(product.getSeller() != null ? product.getSeller().getIdentifier() : null)
                .build();
    }
}
