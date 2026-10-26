package application.adapters.persistence.jpa.adapters;

import application.domain.models.Buyer;
import application.domain.models.ShoppingCart;
import application.domain.ports.out.ShoppingCartRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShoppingCartJpaAdapter implements ShoppingCartRepositoryPort {

    private final Map<String, ShoppingCart> store = new ConcurrentHashMap<>();

    private String getBuyerKey(Buyer buyer) {
        return buyer != null && buyer.getIdentifier() != null ? buyer.getIdentifier() : "";
    }

    @Override
    public ShoppingCart save(ShoppingCart cart) {
        if (cart != null && cart.getBuyer() != null) {
            store.put(getBuyerKey(cart.getBuyer()), cart);
        }
        return cart;
    }

    @Override
    public Optional<ShoppingCart> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(getBuyerKey(buyer)));
    }

    @Override
    public void update(ShoppingCart cart) {
        save(cart);
    }

    @Override
    public void delete(ShoppingCart cart) {
        if (cart != null && cart.getBuyer() != null) {
            store.remove(getBuyerKey(cart.getBuyer()));
        }
    }
}
