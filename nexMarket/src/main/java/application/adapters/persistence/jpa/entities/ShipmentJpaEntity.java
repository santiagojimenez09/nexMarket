package application.adapters.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentJpaEntity {

    @Id
    @Column(name = "identifier", length = 100, nullable = false, unique = true)
    private String identifier;

    @Column(name = "order_identifier", length = 100, nullable = false)
    private String orderIdentifier;

    @Column(name = "warehouse_identifier", length = 100)
    private String warehouseIdentifier;

    @Column(name = "logistics_operator_identifier", length = 100)
    private String logisticsOperatorIdentifier;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "shipment_status", length = 50, nullable = false)
    private String shipmentStatus;

    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
