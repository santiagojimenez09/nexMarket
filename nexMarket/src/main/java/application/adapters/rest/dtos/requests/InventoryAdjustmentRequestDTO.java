package application.adapters.rest.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustmentRequestDTO {
    private String productIdentifier;
    private String warehouseIdentifier;
    private int deltaQuantity;
}
