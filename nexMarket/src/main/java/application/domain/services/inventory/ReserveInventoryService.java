package application.domain.services.inventory;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InsufficientStockException;
import application.domain.ports.in.ReserveInventoryUseCase;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueObjects.InventoryConditionStatus;
import application.domain.valueObjects.InventoryMovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReserveInventoryService implements ReserveInventoryUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort inventoryMovementRepositoryPort;

    public ReserveInventoryService(InventoryRepositoryPort inventoryRepositoryPort,
                                   InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryMovementRepositoryPort = inventoryMovementRepositoryPort;
    }

    public InventoryMovement reserveStock(Inventory inventory, int quantity, Order order, User performedBy) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad a reservar debe ser mayor a cero.");
        }

        Inventory existingInventory = inventoryRepositoryPort.findByProductAndWarehouse(inventory)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado para reservar."));

        if (existingInventory.getConditionStatus() == InventoryConditionStatus.DAMAGED) {
            throw new InsufficientStockException("El stock marcado como DAÑADO (DAMAGED) no puede ser reservado.");
        }

        if (existingInventory.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException(
                    String.format("Stock disponible insuficiente. Disponible: %d, Solicitado: %d",
                            existingInventory.getAvailableQuantity(), quantity));
        }

        existingInventory.setAvailableQuantity(existingInventory.getAvailableQuantity() - quantity);
        existingInventory.setReservedQuantity(existingInventory.getReservedQuantity() + quantity);
        inventoryRepositoryPort.update(existingInventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setIdentifier(UUID.randomUUID().toString());
        movement.setInventory(existingInventory);
        movement.setMovementType(InventoryMovementType.RESERVATION);
        movement.setQuantity(quantity);
        movement.setRelatedOrder(order);
        movement.setPerformedBy(performedBy);
        movement.setMovementDate(LocalDateTime.now());

        return inventoryMovementRepositoryPort.save(movement);
    }
}
