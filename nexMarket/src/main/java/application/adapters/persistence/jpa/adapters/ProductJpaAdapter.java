package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.ProductJpaEntity;
import application.adapters.persistence.jpa.mappers.ProductJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaProductRepository;
import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.valueObjects.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductRepositoryPort {

    private final SpringDataJpaProductRepository productRepository;

    @Override
    public Product save(Product product) {
        if (product == null) return null;
        ProductJpaEntity entity = ProductJpaMapper.toEntity(product);
        ProductJpaEntity saved = productRepository.save(entity);
        return ProductJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findByIdentifier(Product product) {
        if (product == null || product.getIdentifier() == null) return Optional.empty();
        return productRepository.findById(product.getIdentifier()).map(ProductJpaMapper::toDomain);
    }

    @Override
    public List<Product> findBySeller(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) {
            return productRepository.findAll().stream().map(ProductJpaMapper::toDomain).collect(Collectors.toList());
        }
        return productRepository.findBySellerIdentifier(seller.getIdentifier()).stream()
                .map(ProductJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStatus(Product product) {
        if (product == null || product.getStatus() == null) {
            return productRepository.findAll().stream().map(ProductJpaMapper::toDomain).collect(Collectors.toList());
        }
        return productRepository.findByStatus(product.getStatus().name()).stream()
                .map(ProductJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findPublishedCatalog() {
        return productRepository.findByStatus(ProductStatus.PUBLISHED.name()).stream()
                .map(ProductJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Product product) {
        if (product != null && product.getIdentifier() != null) {
            productRepository.save(ProductJpaMapper.toEntity(product));
        }
    }
}
