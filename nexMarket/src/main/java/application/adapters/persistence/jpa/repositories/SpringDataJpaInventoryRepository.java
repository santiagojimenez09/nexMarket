package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaInventoryRepository extends JpaRepository<InventoryJpaEntity, String> {
    Optional<InventoryJpaEntity> findByProductIdentifierAndWarehouseIdentifier(String productIdentifier, String warehouseIdentifier);
    List<InventoryJpaEntity> findByProductIdentifier(String productIdentifier);
    List<InventoryJpaEntity> findByWarehouseIdentifier(String warehouseIdentifier);
}
