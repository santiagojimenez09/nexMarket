package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.SellerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaSellerRepository extends JpaRepository<SellerJpaEntity, String> {
    List<SellerJpaEntity> findByRegisteredByIdentifier(String registeredByIdentifier);
}
