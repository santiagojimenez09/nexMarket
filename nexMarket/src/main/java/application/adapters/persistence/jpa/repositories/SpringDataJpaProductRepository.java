package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaProductRepository extends JpaRepository<ProductJpaEntity, String> {
    List<ProductJpaEntity> findBySellerIdentifier(String sellerIdentifier);
    List<ProductJpaEntity> findByStatus(String status);
}
