package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.ShipmentJpaEntity;
import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.models.Warehouse;
import application.domain.valueObjects.ShipmentStatus;

public class ShipmentJpaMapper {

    public static ShipmentJpaEntity toEntity(Shipment domain) {
        if (domain == null) return null;
        String orderId = domain.getOrder() != null ? domain.getOrder().getIdentifier() : null;
        String warehouseId = domain.getOriginWarehouse() != null ? domain.getOriginWarehouse().getIdentifier() : null;
        String operatorId = domain.getLogisticsOperator() != null ? domain.getLogisticsOperator().getIdentifier() : null;

        return ShipmentJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .orderIdentifier(orderId)
                .warehouseIdentifier(warehouseId)
                .logisticsOperatorIdentifier(operatorId)
                .destinationAddress(domain.getDestinationAddress())
                .shipmentStatus(domain.getShipmentStatus() != null ? domain.getShipmentStatus().name() : ShipmentStatus.IN_PREPARATION.name())
                .dispatchDate(domain.getDispatchDate())
                .deliveryDate(domain.getDeliveryDate())
                .build();
    }

    public static Shipment toDomain(ShipmentJpaEntity entity) {
        if (entity == null) return null;
        Shipment shipment = new Shipment();
        shipment.setIdentifier(entity.getIdentifier());
        shipment.setDestinationAddress(entity.getDestinationAddress());
        shipment.setDispatchDate(entity.getDispatchDate());
        shipment.setDeliveryDate(entity.getDeliveryDate());

        if (entity.getShipmentStatus() != null) {
            try {
                shipment.setShipmentStatus(ShipmentStatus.valueOf(entity.getShipmentStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getOrderIdentifier() != null) {
            Order order = new Order();
            order.setIdentifier(entity.getOrderIdentifier());
            shipment.setOrder(order);
        }
        if (entity.getWarehouseIdentifier() != null) {
            Warehouse wh = new Warehouse();
            wh.setIdentifier(entity.getWarehouseIdentifier());
            shipment.setOriginWarehouse(wh);
        }
        if (entity.getLogisticsOperatorIdentifier() != null) {
            LogisticsOperator op = new LogisticsOperator();
            op.setIdentifier(entity.getLogisticsOperatorIdentifier());
            shipment.setLogisticsOperator(op);
        }
        return shipment;
    }
}
