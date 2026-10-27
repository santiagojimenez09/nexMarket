package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.ShoppingCartJpaEntity;
import application.adapters.persistence.jpa.mappers.ShoppingCartJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaShoppingCartRepository;
import application.domain.models.Buyer;
import application.domain.models.ShoppingCart;
import application.domain.ports.out.ShoppingCartRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShoppingCartJpaAdapter implements ShoppingCartRepositoryPort {

    private final SpringDataJpaShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart save(ShoppingCart cart) {
        if (cart == null) return null;
        ShoppingCartJpaEntity entity = ShoppingCartJpaMapper.toEntity(cart);
        ShoppingCartJpaEntity saved = shoppingCartRepository.save(entity);
        return ShoppingCartJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<ShoppingCart> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return Optional.empty();
        return shoppingCartRepository.findByBuyerIdentifier(buyer.getIdentifier())
                .map(ShoppingCartJpaMapper::toDomain);
    }

    @Override
    public void update(ShoppingCart cart) {
        if (cart != null && cart.getBuyer() != null && cart.getBuyer().getIdentifier() != null) {
            shoppingCartRepository.save(ShoppingCartJpaMapper.toEntity(cart));
        }
    }

    @Override
    public void delete(ShoppingCart cart) {
        if (cart != null && cart.getBuyer() != null && cart.getBuyer().getIdentifier() != null) {
            shoppingCartRepository.deleteById(cart.getBuyer().getIdentifier());
        }
    }
}
