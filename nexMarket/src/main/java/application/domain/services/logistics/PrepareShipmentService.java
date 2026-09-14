package application.domain.services.logistics;

import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.models.Warehouse;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.in.PrepareShipmentUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueObjects.OrderStatus;
import application.domain.valueObjects.ShipmentStatus;

import java.util.UUID;

public class PrepareShipmentService implements PrepareShipmentUseCase {

    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public PrepareShipmentService(ShipmentRepositoryPort shipmentRepositoryPort,
                                  OrderRepositoryPort orderRepositoryPort,
                                  WarehouseRepositoryPort warehouseRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Shipment prepareShipment(Order order, Warehouse warehouse, LogisticsOperator operator, String destinationAddress) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() != OrderStatus.PAID) {
            throw new DomainException("Solo se pueden preparar envíos para pedidos pagados.");
        }

        Warehouse existingWarehouse = warehouseRepositoryPort.findByIdentifier(warehouse)
                .orElseThrow(() -> new EntityNotFoundException("Almacén no encontrado: " + warehouse.getIdentifier()));

        Shipment shipment = new Shipment();
        shipment.setIdentifier("SHP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        shipment.setOrder(existingOrder);
        shipment.setOriginWarehouse(existingWarehouse);
        shipment.setLogisticsOperator(operator);
        shipment.setDestinationAddress(destinationAddress != null ? destinationAddress : existingOrder.getBuyer().getPrimaryAddress());
        shipment.setShipmentStatus(ShipmentStatus.IN_PREPARATION);

        return shipmentRepositoryPort.save(shipment);
    }
}
