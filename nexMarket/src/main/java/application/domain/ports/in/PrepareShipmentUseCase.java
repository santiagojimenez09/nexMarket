package application.domain.ports.in;

import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.models.Warehouse;

public interface PrepareShipmentUseCase {
    Shipment prepareShipment(Order order, Warehouse warehouse, LogisticsOperator operator, String destinationAddress);
}
