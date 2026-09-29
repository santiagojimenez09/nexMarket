package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private String identifier;
    private String buyerIdentifier;
    private String orderStatus;
    private LocalDateTime creationDate;
    private List<OrderItemResponseDTO> items;
}
