package application.domain.services.inventory;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueObjects.InventoryMovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterInboundMovementService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;

    public RegisterInboundMovementService(InventoryRepositoryPort inventoryRepositoryPort,
                                          InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryMovementRepositoryPort = inventoryMovementRepositoryPort;
    }

    public InventoryMovement registerInbound(Inventory inventory, int quantity, User performedBy) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad de ingreso de inventario debe ser mayor a cero.");
        }

        Inventory existingInventory = inventoryRepositoryPort.findByProductAndWarehouse(inventory)
                .orElseThrow(() -> new EntityNotFoundException("No existe registro de inventario para este producto en el almacén indicado."));

        existingInventory.setAvailableQuantity(existingInventory.getAvailableQuantity() + quantity);
        inventoryRepositoryPort.update(existingInventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setIdentifier(UUID.randomUUID().toString());
        movement.setInventory(existingInventory);
        movement.setMovementType(InventoryMovementType.INBOUND);
        movement.setQuantity(quantity);
        movement.setPerformedBy(performedBy);
        movement.setMovementDate(LocalDateTime.now());

        return inventoryMovementRepositoryPort.save(movement);
    }
}
