package application.domain.services.cart;

import application.domain.models.Buyer;
import application.domain.models.CartItem;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.in.AddItemToCartUseCase;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ShoppingCartRepositoryPort;

import java.time.LocalDateTime;

public class AddItemToCartService implements AddItemToCartUseCase {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;

    public AddItemToCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort,
                                ProductRepositoryPort productRepositoryPort,
                                BuyerRepositoryPort buyerRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.buyerRepositoryPort = buyerRepositoryPort;
    }

    public ShoppingCart addItem(Buyer buyer, Product product, int quantity) {
        if (quantity <= 0) {
            throw new DomainException("La cantidad debe ser mayor que cero.");
        }

        Buyer existingBuyer = buyerRepositoryPort.findByIdentifier(buyer)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado: " + buyer.getIdentifier()));

        Product existingProduct = productRepositoryPort.findByIdentifier(product)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + product.getIdentifier()));

        ShoppingCart cart = shoppingCartRepositoryPort.findByBuyer(existingBuyer)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setBuyer(existingBuyer);
                    newCart.setLastUpdated(LocalDateTime.now());
                    return shoppingCartRepositoryPort.save(newCart);
                });

        boolean itemFound = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getIdentifier().equals(existingProduct.getIdentifier())) {
                item.setQuantity(item.getQuantity() + quantity);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            CartItem newItem = new CartItem();
            newItem.setProduct(existingProduct);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cart.setLastUpdated(LocalDateTime.now());
        shoppingCartRepositoryPort.update(cart);
        existingBuyer.setCart(cart);

        return cart;
    }
}
