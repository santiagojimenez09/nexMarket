package application.domain.services.logistics;

import application.domain.models.Shipment;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.ShipmentRepositoryPort;
import application.domain.valueObjects.ShipmentStatus;

import java.time.LocalDateTime;

public class DispatchShipmentService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;

    public DispatchShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
    }

    public Shipment dispatchShipment(Shipment shipment) {
        Shipment existingShipment = shipmentRepositoryPort.findByIdentifier(shipment)
                .orElseThrow(() -> new EntityNotFoundException("Envío no encontrado: " + shipment.getIdentifier()));

        if (existingShipment.getShipmentStatus() != ShipmentStatus.IN_PREPARATION) {
            throw new InvalidStatusTransitionException("Solo envíos en preparación pueden ser despachados. Estado actual: " + existingShipment.getShipmentStatus());
        }

        existingShipment.setShipmentStatus(ShipmentStatus.DISPATCHED);
        existingShipment.setDispatchDate(LocalDateTime.now());
        shipmentRepositoryPort.update(existingShipment);

        return existingShipment;
    }
}
