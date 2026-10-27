package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.OperationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaOperationRepository extends JpaRepository<OperationJpaEntity, String> {
    List<OperationJpaEntity> findByPerformedByIdentifier(String performedByIdentifier);
    List<OperationJpaEntity> findByAffectedProcessIdentifier(String affectedProcessIdentifier);
    List<OperationJpaEntity> findByOperationType(String operationType);
}
