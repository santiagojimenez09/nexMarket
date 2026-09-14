package application.domain.ports.out;

import application.domain.models.Buyer;
import application.domain.models.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepositoryPort {
    ShoppingCart save(ShoppingCart cart);
    Optional<ShoppingCart> findByBuyer(Buyer buyer);
    void update(ShoppingCart cart);
    void delete(ShoppingCart cart);
}
