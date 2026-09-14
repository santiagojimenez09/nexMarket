package application.domain.models;

import application.domain.valueObjects.InventoryMovementType;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryMovement {
    private String identifier;
    private Inventory inventory;
    private InventoryMovementType movementType;
    private Integer quantity;
    private Order relatedOrder;
    private User performedBy;
    private LocalDateTime movementDate;
}
