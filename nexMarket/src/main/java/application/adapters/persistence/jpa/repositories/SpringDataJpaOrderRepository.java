package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaOrderRepository extends JpaRepository<OrderJpaEntity, String> {
    List<OrderJpaEntity> findByBuyerIdentifier(String buyerIdentifier);
    List<OrderJpaEntity> findByOrderStatus(String orderStatus);
}
