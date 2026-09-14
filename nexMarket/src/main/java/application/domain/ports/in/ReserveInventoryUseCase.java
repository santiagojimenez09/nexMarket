package application.domain.ports.in;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.models.User;

public interface ReserveInventoryUseCase {
    InventoryMovement reserveStock(Inventory inventory, int quantity, Order order, User performedBy);
}
