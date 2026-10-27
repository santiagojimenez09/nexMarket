package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.ReturnJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaReturnRepository extends JpaRepository<ReturnJpaEntity, String> {
    List<ReturnJpaEntity> findByOrderIdentifier(String orderIdentifier);
    List<ReturnJpaEntity> findByReturnStatus(String returnStatus);
}
