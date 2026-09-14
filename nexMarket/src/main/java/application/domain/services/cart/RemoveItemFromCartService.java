package application.domain.services.cart;

import application.domain.models.Buyer;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ShoppingCartRepositoryPort;

import java.time.LocalDateTime;

public class RemoveItemFromCartService {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;

    public RemoveItemFromCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
    }

    public ShoppingCart removeItem(Buyer buyer, Product product) {
        ShoppingCart cart = shoppingCartRepositoryPort.findByBuyer(buyer)
                .orElseThrow(() -> new EntityNotFoundException("El comprador no posee un carrito activo."));

        boolean removed = cart.getItems().removeIf(item -> item.getProduct().getIdentifier().equals(product.getIdentifier()));

        if (!removed) {
            throw new EntityNotFoundException("El producto indicado no se encuentra en el carrito.");
        }

        cart.setLastUpdated(LocalDateTime.now());
        shoppingCartRepositoryPort.update(cart);
        return cart;
    }
}
