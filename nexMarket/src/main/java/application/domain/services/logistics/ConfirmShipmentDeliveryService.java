package application.domain.services.logistics;

import application.domain.models.Shipment;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.services.order.ConfirmDeliveryService;
import application.domain.valueObjects.ShipmentStatus;

import java.time.LocalDateTime;

public class ConfirmShipmentDeliveryService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;
    private final ConfirmDeliveryService confirmDeliveryService;

    public ConfirmShipmentDeliveryService(ShipmentRepositoryPort shipmentRepositoryPort,
                                         ConfirmDeliveryService confirmDeliveryService) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
        this.confirmDeliveryService = confirmDeliveryService;
    }

    public Shipment confirmDelivery(Shipment shipment) {
        Shipment existingShipment = shipmentRepositoryPort.findByIdentifier(shipment)
                .orElseThrow(() -> new EntityNotFoundException("Envío no encontrado: " + shipment.getIdentifier()));

        if (existingShipment.getShipmentStatus() == ShipmentStatus.DELIVERED) {
            throw new InvalidStatusTransitionException("El envío ya ha sido entregado previamente.");
        }

        existingShipment.setShipmentStatus(ShipmentStatus.DELIVERED);
        existingShipment.setDeliveryDate(LocalDateTime.now());
        shipmentRepositoryPort.update(existingShipment);

        // Transition related order to DELIVERED
        if (existingShipment.getOrder() != null) {
            confirmDeliveryService.confirmDelivery(existingShipment.getOrder());
        }

        return existingShipment;
    }
}
