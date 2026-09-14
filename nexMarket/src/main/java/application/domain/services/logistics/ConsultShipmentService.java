package application.domain.services.logistics;

import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ShipmentRepositoryPort;

import java.util.List;

public class ConsultShipmentService {

    private final ShipmentRepositoryPort shipmentRepositoryPort;

    public ConsultShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
    }

    public Shipment getByIdentifier(Shipment shipment) {
        return shipmentRepositoryPort.findByIdentifier(shipment)
                .orElseThrow(() -> new EntityNotFoundException("Envío no encontrado: " + shipment.getIdentifier()));
    }

    public List<Shipment> getByOrder(Order order) {
        return shipmentRepositoryPort.findByOrder(order);
    }

    public List<Shipment> getByOperator(LogisticsOperator operator) {
        return shipmentRepositoryPort.findByLogisticsOperator(operator);
    }
}
