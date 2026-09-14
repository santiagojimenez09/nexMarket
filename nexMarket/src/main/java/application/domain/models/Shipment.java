package application.domain.models;

import application.domain.valueObjects.ShipmentStatus;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Shipment extends TrackableProcess {
    private Order order;
    private Warehouse originWarehouse;
    private LogisticsOperator logisticsOperator;
    private String destinationAddress;
    private ShipmentStatus shipmentStatus;
    private LocalDateTime dispatchDate;
    private LocalDateTime deliveryDate;
}
