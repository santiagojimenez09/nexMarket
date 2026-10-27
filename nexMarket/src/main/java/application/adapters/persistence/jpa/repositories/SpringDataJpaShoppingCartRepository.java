package application.adapters.persistence.jpa.repositories;

import application.adapters.persistence.jpa.entities.ShoppingCartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataJpaShoppingCartRepository extends JpaRepository<ShoppingCartJpaEntity, String> {
    Optional<ShoppingCartJpaEntity> findByBuyerIdentifier(String buyerIdentifier);
}
