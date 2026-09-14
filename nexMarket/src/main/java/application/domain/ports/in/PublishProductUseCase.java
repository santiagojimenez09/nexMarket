package application.domain.ports.in;

import application.domain.models.Product;

public interface PublishProductUseCase {
    void publishProduct(Product product);
}
