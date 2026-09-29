package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String identifier;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String status;
    private String productType;
    private String sellerIdentifier;
}
