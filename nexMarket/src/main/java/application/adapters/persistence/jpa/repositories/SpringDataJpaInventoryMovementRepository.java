package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.InventoryMovementJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaInventoryMovementRepository extends JpaRepository<InventoryMovementJpaEntity, String> {
    List<InventoryMovementJpaEntity> findByInventoryIdentifier(String inventoryIdentifier);
    List<InventoryMovementJpaEntity> findByRelatedOrderIdentifier(String relatedOrderIdentifier);
    List<InventoryMovementJpaEntity> findByMovementType(String movementType);
}
