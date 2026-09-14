package application.domain.services.catalog;

import application.domain.models.Product;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.ProductStatus;

public class DiscontinueProductService {

    private final ProductRepositoryPort productRepositoryPort;

    public DiscontinueProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public void discontinueProduct(Product product) {
        Product existingProduct = productRepositoryPort.findByIdentifier(product)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + product.getIdentifier()));

        existingProduct.setStatus(ProductStatus.DISCONTINUED);
        productRepositoryPort.update(existingProduct);
    }
}
