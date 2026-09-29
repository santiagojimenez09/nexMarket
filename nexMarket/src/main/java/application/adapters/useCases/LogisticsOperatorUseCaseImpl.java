package application.adapters.useCases;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.models.User;
import application.domain.models.Warehouse;
import application.domain.ports.in.LogisticsOperatorPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.services.inventory.ConsultInventoryService;
import application.domain.services.inventory.RegisterInboundMovementService;
import application.domain.services.inventory.RegisterInventoryAdjustmentService;
import application.domain.services.inventory.RegisterReturnInboundMovementService;
import application.domain.services.logistics.ConfirmShipmentDeliveryService;
import application.domain.services.logistics.ConsultShipmentService;
import application.domain.services.logistics.DispatchShipmentService;
import application.domain.services.logistics.PrepareShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsOperatorUseCaseImpl implements LogisticsOperatorPort {

    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final ConsultInventoryService consultInventoryService;
    private final RegisterInboundMovementService registerInboundMovementService;
    private final RegisterInventoryAdjustmentService registerInventoryAdjustmentService;
    private final RegisterReturnInboundMovementService registerReturnInboundMovementService;
    private final PrepareShipmentService prepareShipmentService;
    private final DispatchShipmentService dispatchShipmentService;
    private final ConfirmShipmentDeliveryService confirmShipmentDeliveryService;
    private final ConsultShipmentService consultShipmentService;

    @Override
    public List<Warehouse> consultOperatedWarehouses(User user) {
        return warehouseRepositoryPort.findByType(new Warehouse());
    }

    @Override
    public Inventory consultWarehouseInventory(User user, Inventory query) {
        return consultInventoryService.getByProductAndWarehouse(query);
    }

    @Override
    public InventoryMovement registerInboundStock(User user, Inventory inventory, int quantity) {
        return registerInboundMovementService.registerInbound(inventory, quantity, user);
    }

    @Override
    public InventoryMovement registerInventoryAdjustment(User user, Inventory inventory, int deltaQuantity) {
        return registerInventoryAdjustmentService.adjustStock(inventory, deltaQuantity, user);
    }

    @Override
    public InventoryMovement registerReturnInbound(User user, Inventory inventory, int quantity, Order order) {
        return registerReturnInboundMovementService.registerReturnInbound(inventory, quantity, order, user);
    }

    @Override
    public Shipment prepareShipment(User user, Order order, Warehouse warehouse, String destinationAddress) {
        LogisticsOperator operator = new LogisticsOperator();
        operator.setIdentifier(user.getIdentifier());
        operator.setFullName(user.getFullName());
        operator.setEmail(user.getEmail());
        return prepareShipmentService.prepareShipment(order, warehouse, operator, destinationAddress);
    }

    @Override
    public Shipment dispatchShipment(User user, Shipment shipment) {
        return dispatchShipmentService.dispatchShipment(shipment);
    }

    @Override
    public Shipment confirmDelivery(User user, Shipment shipment) {
        return confirmShipmentDeliveryService.confirmDelivery(shipment);
    }

    @Override
    public List<Shipment> consultAssignedShipments(User user) {
        LogisticsOperator operator = new LogisticsOperator();
        operator.setIdentifier(user.getIdentifier());
        return consultShipmentService.getByOperator(operator);
    }
}
