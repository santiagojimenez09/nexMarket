package application.domain.services.catalog;

import application.domain.models.Product;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.ProductStatus;

public class SuspendProductService {

    private final ProductRepositoryPort productRepositoryPort;

    public SuspendProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public void suspendProduct(Product product) {
        Product existingProduct = productRepositoryPort.findByIdentifier(product)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + product.getIdentifier()));

        existingProduct.setStatus(ProductStatus.SUSPENDED);
        productRepositoryPort.update(existingProduct);
    }
}
