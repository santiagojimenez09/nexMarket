package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.CartItemEmbeddable;
import application.adapters.persistence.jpa.entities.ShoppingCartJpaEntity;
import application.domain.models.Buyer;
import application.domain.models.CartItem;
import application.domain.models.PhysicalProduct;
import application.domain.models.ShoppingCart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartJpaMapper {

    public static ShoppingCartJpaEntity toEntity(ShoppingCart domain) {
        if (domain == null) return null;
        String buyerId = domain.getBuyer() != null ? domain.getBuyer().getIdentifier() : null;

        List<CartItemEmbeddable> itemEntities = new ArrayList<>();
        if (domain.getItems() != null) {
            itemEntities = domain.getItems().stream()
                    .filter(i -> i != null && i.getProduct() != null)
                    .map(i -> CartItemEmbeddable.builder()
                            .productIdentifier(i.getProduct().getIdentifier())
                            .quantity(i.getQuantity())
                            .build())
                    .collect(Collectors.toList());
        }

        return ShoppingCartJpaEntity.builder()
                .buyerIdentifier(buyerId)
                .lastUpdated(domain.getLastUpdated())
                .items(itemEntities)
                .build();
    }

    public static ShoppingCart toDomain(ShoppingCartJpaEntity entity) {
        if (entity == null) return null;
        ShoppingCart cart = new ShoppingCart();
        cart.setLastUpdated(entity.getLastUpdated());

        if (entity.getBuyerIdentifier() != null) {
            Buyer buyer = new Buyer();
            buyer.setIdentifier(entity.getBuyerIdentifier());
            cart.setBuyer(buyer);
        }

        List<CartItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(i -> {
                        PhysicalProduct p = new PhysicalProduct();
                        p.setIdentifier(i.getProductIdentifier());
                        return new CartItem(p, i.getQuantity());
                    })
                    .collect(Collectors.toList());
        }
        cart.setItems(items);
        return cart;
    }
}
