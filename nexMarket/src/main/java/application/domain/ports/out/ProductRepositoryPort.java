package application.domain.ports.out;

import application.domain.models.Product;
import application.domain.models.Seller;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findByIdentifier(Product product);
    List<Product> findBySeller(Seller seller);
    List<Product> findByStatus(Product product);
    List<Product> findPublishedCatalog();
    void update(Product product);
}
