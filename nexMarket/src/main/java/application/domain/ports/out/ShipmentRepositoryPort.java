package application.domain.ports.out;

import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);
    Optional<Shipment> findByIdentifier(Shipment shipment);
    List<Shipment> findByOrder(Order order);
    List<Shipment> findByLogisticsOperator(LogisticsOperator operator);
    void update(Shipment shipment);
}
