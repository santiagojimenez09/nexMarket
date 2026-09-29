package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {
    private String productIdentifier;
    private String warehouseIdentifier;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private String conditionStatus;
}
