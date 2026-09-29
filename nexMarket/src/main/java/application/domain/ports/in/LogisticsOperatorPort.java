package application.domain.ports.in;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.models.User;
import application.domain.models.Warehouse;
import java.util.List;

public interface LogisticsOperatorPort {
    List<Warehouse> consultOperatedWarehouses(User user);
    
    // Inventory Operations
    Inventory consultWarehouseInventory(User user, Inventory query);
    InventoryMovement registerInboundStock(User user, Inventory inventory, int quantity);
    InventoryMovement registerInventoryAdjustment(User user, Inventory inventory, int deltaQuantity);
    InventoryMovement registerReturnInbound(User user, Inventory inventory, int quantity, Order order);
    
    // Shipment & Dispatching
    Shipment prepareShipment(User user, Order order, Warehouse warehouse, String destinationAddress);
    Shipment dispatchShipment(User user, Shipment shipment);
    Shipment confirmDelivery(User user, Shipment shipment);
    List<Shipment> consultAssignedShipments(User user);
}
