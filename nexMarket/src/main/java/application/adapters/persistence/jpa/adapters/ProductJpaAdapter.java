package application.adapters.persistence.jpa.adapters;

import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.ProductStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ProductJpaAdapter implements ProductRepositoryPort {

    private final Map<String, Product> store = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        if (product == null || product.getIdentifier() == null) return product;
        store.put(product.getIdentifier(), product);
        return product;
    }

    @Override
    public Optional<Product> findByIdentifier(Product product) {
        if (product == null || product.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(product.getIdentifier()));
    }

    @Override
    public List<Product> findBySeller(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) {
            return List.copyOf(store.values());
        }
        return store.values().stream()
                .filter(p -> p.getSeller() != null && seller.getIdentifier().equals(p.getSeller().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStatus(Product product) {
        if (product == null || product.getStatus() == null) {
            return List.copyOf(store.values());
        }
        return store.values().stream()
                .filter(p -> product.getStatus().equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findPublishedCatalog() {
        return store.values().stream()
                .filter(p -> ProductStatus.PUBLISHED.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Product product) {
        if (product != null && product.getIdentifier() != null) {
            store.put(product.getIdentifier(), product);
        }
    }
}
