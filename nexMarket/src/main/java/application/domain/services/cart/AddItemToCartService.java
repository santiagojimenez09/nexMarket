package application.domain.services.cart;

import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InsufficientStockException;
import application.domain.models.Buyer;
import application.domain.models.CartItem;
import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;
import application.domain.ports.in.AddItemToCartUseCase;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ShoppingCartRepositoryPort;
import application.domain.valueObjects.ProductType;

import java.time.LocalDateTime;

public class AddItemToCartService implements AddItemToCartUseCase {

    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;

    public AddItemToCartService(ShoppingCartRepositoryPort shoppingCartRepositoryPort,
                                ProductRepositoryPort productRepositoryPort,
                                BuyerRepositoryPort buyerRepositoryPort,
                                InventoryRepositoryPort inventoryRepositoryPort) {
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.inventoryRepositoryPort = inventoryRepositoryPort;
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

        int currentQuantityInCart = 0;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getIdentifier().equals(existingProduct.getIdentifier())) {
                currentQuantityInCart = item.getQuantity();
                break;
            }
        }
        int totalRequestedQuantity = currentQuantityInCart + quantity;

        // Validar inventario disponible si es un producto físico
        if (existingProduct.getProductType() == ProductType.PHYSICAL && inventoryRepositoryPort != null) {
            int availableStock = inventoryRepositoryPort.findByProduct(existingProduct).stream()
                    .mapToInt(Inventory::getAvailableQuantity)
                    .sum();
            if (totalRequestedQuantity > availableStock) {
                throw new InsufficientStockException(String.format(
                        "Stock disponible insuficiente en inventario para el producto físico %s (ID: %s). Disponible: %d, Solicitado: %d",
                        existingProduct.getName(), existingProduct.getIdentifier(), availableStock, totalRequestedQuantity));
            }
        }

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
