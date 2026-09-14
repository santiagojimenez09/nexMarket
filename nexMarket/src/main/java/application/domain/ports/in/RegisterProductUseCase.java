package application.domain.ports.in;

import application.domain.models.Product;
import application.domain.models.Seller;

public interface RegisterProductUseCase {
    Product registerProduct(Product product, Seller seller);
}
