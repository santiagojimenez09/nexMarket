package application.domain.services.cart;

import application.domain.models.Buyer;
import application.domain.models.CartItem;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ShoppingCartRepositoryPort;

import java.time.LocalDateTime;

public class UpdateCartItemService {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;

    public UpdateCartItemService(ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
    }

    public ShoppingCart updateItemQuantity(Buyer buyer, Product product, int newQuantity) {
        if (newQuantity <= 0) {
            throw new DomainException("Para eliminar el ítem use el servicio de eliminación o indique cantidad mayor a cero.");
        }

        ShoppingCart cart = shoppingCartRepositoryPort.findByBuyer(buyer)
                .orElseThrow(() -> new EntityNotFoundException("El comprador no posee un carrito activo."));

        boolean updated = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getIdentifier().equals(product.getIdentifier())) {
                item.setQuantity(newQuantity);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new EntityNotFoundException("El producto indicado no se encuentra en el carrito.");
        }

        cart.setLastUpdated(LocalDateTime.now());
        shoppingCartRepositoryPort.update(cart);
        return cart;
    }
}
