package application.domain.services.cart;

import application.domain.models.Buyer;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ShoppingCartRepositoryPort;

public class ConsultCartService {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;

    public ConsultCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
    }

    public ShoppingCart getCartByBuyer(Buyer buyer) {
        return shoppingCartRepositoryPort.findByBuyer(buyer)
                .orElseThrow(() -> new EntityNotFoundException("El comprador no posee un carrito activo."));
    }
}
