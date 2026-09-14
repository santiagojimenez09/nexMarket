package application.domain.services.cart;

import application.domain.models.Buyer;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ShoppingCartRepositoryPort;

import java.time.LocalDateTime;

public class ClearCartService {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;

    public ClearCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
    }

    public void clearCart(Buyer buyer) {
        ShoppingCart cart = shoppingCartRepositoryPort.findByBuyer(buyer)
                .orElseThrow(() -> new EntityNotFoundException("El comprador no posee un carrito activo."));

        cart.getItems().clear();
        cart.setLastUpdated(LocalDateTime.now());
        shoppingCartRepositoryPort.update(cart);
    }
}
