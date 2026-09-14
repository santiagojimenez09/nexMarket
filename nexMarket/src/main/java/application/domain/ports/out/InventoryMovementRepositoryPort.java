package application.domain.ports.out;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;

import java.util.List;

public interface InventoryMovementRepositoryPort {
    InventoryMovement save(InventoryMovement movement);
    List<InventoryMovement> findByInventory(Inventory inventory);
    List<InventoryMovement> findByOrder(Order order);
    List<InventoryMovement> findByType(InventoryMovement movement);
}
