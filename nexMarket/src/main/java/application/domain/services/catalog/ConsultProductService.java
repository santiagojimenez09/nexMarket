package application.domain.services.catalog;

import application.domain.models.Product;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ProductRepositoryPort;

public class ConsultProductService {

    private final ProductRepositoryPort productRepositoryPort;

    public ConsultProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public Product getByIdentifier(Product product) {
        return productRepositoryPort.findByIdentifier(product)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + product.getIdentifier()));
    }
}
