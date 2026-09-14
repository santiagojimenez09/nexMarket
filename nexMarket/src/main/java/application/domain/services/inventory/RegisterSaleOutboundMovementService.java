package application.domain.services.inventory;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueObjects.InventoryMovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterSaleOutboundMovementService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;

    public RegisterSaleOutboundMovementService(InventoryRepositoryPort inventoryRepositoryPort,
                                               InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryMovementRepositoryPort = inventoryMovementRepositoryPort;
    }

    public InventoryMovement registerSaleOutbound(Inventory inventory, int quantity, Order order, User performedBy) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad de salida por venta debe ser mayor a cero.");
        }

        Inventory existingInventory = inventoryRepositoryPort.findByProductAndWarehouse(inventory)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado para registrar salida por venta."));

        if (existingInventory.getReservedQuantity() < quantity) {
            throw new DomainException("La cantidad reservada es inferior a la cantidad de salida solicitada.");
        }

        existingInventory.setReservedQuantity(existingInventory.getReservedQuantity() - quantity);
        inventoryRepositoryPort.update(existingInventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setIdentifier(UUID.randomUUID().toString());
        movement.setInventory(existingInventory);
        movement.setMovementType(InventoryMovementType.SALE_OUTBOUND);
        movement.setQuantity(quantity);
        movement.setRelatedOrder(order);
        movement.setPerformedBy(performedBy);
        movement.setMovementDate(LocalDateTime.now());

        return inventoryMovementRepositoryPort.save(movement);
    }
}
