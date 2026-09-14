package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Product;
import application.domain.models.ShoppingCart;

public interface AddItemToCartUseCase {
    ShoppingCart addItem(Buyer buyer, Product product, int quantity);
}
