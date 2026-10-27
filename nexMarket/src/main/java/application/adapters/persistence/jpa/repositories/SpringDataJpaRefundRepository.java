package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.RefundJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaRefundRepository extends JpaRepository<RefundJpaEntity, String> {
    Optional<RefundJpaEntity> findByReturnIdentifier(String returnIdentifier);
    List<RefundJpaEntity> findByRefundStatus(String refundStatus);
}
