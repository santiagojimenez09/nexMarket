package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponseDTO {
    private String identifier;
    private String orderIdentifier;
    private String originWarehouseIdentifier;
    private String shipmentStatus;
    private String destinationAddress;
    private LocalDateTime dispatchDate;
    private LocalDateTime deliveryDate;
}
