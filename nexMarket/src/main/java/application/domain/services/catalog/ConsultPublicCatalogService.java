package application.domain.services.catalog;

import application.domain.models.Product;
import application.domain.ports.out.ProductRepositoryPort;

import java.util.List;

public class ConsultPublicCatalogService {

    private final ProductRepositoryPort productRepositoryPort;

    public ConsultPublicCatalogService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    public List<Product> getPublicCatalog() {
        return productRepositoryPort.findPublishedCatalog();
    }
}
