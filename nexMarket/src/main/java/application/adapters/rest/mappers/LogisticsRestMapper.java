package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.InventoryResponseDTO;
import application.adapters.rest.dtos.responses.ShipmentResponseDTO;
import application.domain.models.Inventory;
import application.domain.models.Shipment;

public class LogisticsRestMapper {

    public static ShipmentResponseDTO toResponseDTO(Shipment shipment) {
        if (shipment == null) return null;
        return ShipmentResponseDTO.builder()
                .identifier(shipment.getIdentifier())
                .orderIdentifier(shipment.getOrder() != null ? shipment.getOrder().getIdentifier() : null)
                .originWarehouseIdentifier(shipment.getOriginWarehouse() != null ? shipment.getOriginWarehouse().getIdentifier() : null)
                .shipmentStatus(shipment.getShipmentStatus() != null ? shipment.getShipmentStatus().name() : null)
                .destinationAddress(shipment.getDestinationAddress())
                .dispatchDate(shipment.getDispatchDate())
                .deliveryDate(shipment.getDeliveryDate())
                .build();
    }

    public static InventoryResponseDTO toInventoryResponseDTO(Inventory inventory) {
        if (inventory == null) return null;
        return InventoryResponseDTO.builder()
                .productIdentifier(inventory.getProduct() != null ? inventory.getProduct().getIdentifier() : null)
                .warehouseIdentifier(inventory.getWarehouse() != null ? inventory.getWarehouse().getIdentifier() : null)
                .availableQuantity(inventory.getAvailableQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .conditionStatus(inventory.getConditionStatus() != null ? inventory.getConditionStatus().name() : null)
                .build();
    }
}
