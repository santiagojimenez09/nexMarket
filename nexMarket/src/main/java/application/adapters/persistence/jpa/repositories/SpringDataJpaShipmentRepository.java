package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaShipmentRepository extends JpaRepository<ShipmentJpaEntity, String> {
    List<ShipmentJpaEntity> findByOrderIdentifier(String orderIdentifier);
    List<ShipmentJpaEntity> findByLogisticsOperatorIdentifier(String logisticsOperatorIdentifier);
}
