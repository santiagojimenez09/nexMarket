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
@Table(name = "inventory_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementJpaEntity {

    @Id
    @Column(name = "identifier", length = 100, nullable = false, unique = true)
    private String identifier;

    @Column(name = "inventory_identifier", length = 100)
    private String inventoryIdentifier;

    @Column(name = "movement_type", length = 50, nullable = false)
    private String movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "related_order_identifier", length = 100)
    private String relatedOrderIdentifier;

    @Column(name = "performed_by_identifier", length = 100)
    private String performedByIdentifier;

    @Column(name = "movement_date")
    private LocalDateTime movementDate;
}
