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

public class RegisterInventoryAdjustmentService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;

    public RegisterInventoryAdjustmentService(InventoryRepositoryPort inventoryRepositoryPort,
                                              InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryMovementRepositoryPort = inventoryMovementRepositoryPort;
    }

    public InventoryMovement adjustStock(Inventory inventory, int deltaQuantity, User performedBy) {
        Inventory existingInventory = inventoryRepositoryPort.findByProductAndWarehouse(inventory)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado para ajuste."));

        int newAvailable = existingInventory.getAvailableQuantity() + deltaQuantity;
        if (newAvailable < 0) {
            throw new DomainException("El ajuste de inventario resultaría en stock negativo, lo cual está prohibido.");
        }

        existingInventory.setAvailableQuantity(newAvailable);
        inventoryRepositoryPort.update(existingInventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setIdentifier(UUID.randomUUID().toString());
        movement.setInventory(existingInventory);
        movement.setMovementType(InventoryMovementType.ADJUSTMENT);
        movement.setQuantity(deltaQuantity);
        movement.setPerformedBy(performedBy);
        movement.setMovementDate(LocalDateTime.now());

        return inventoryMovementRepositoryPort.save(movement);
    }
}
