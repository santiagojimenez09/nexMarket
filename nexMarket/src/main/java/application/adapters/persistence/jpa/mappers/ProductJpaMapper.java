package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.ProductJpaEntity;
import application.domain.models.DigitalProduct;
import application.domain.models.PhysicalProduct;
import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;

public class ProductJpaMapper {

    public static ProductJpaEntity toEntity(Product domain) {
        if (domain == null) return null;
        String sellerId = domain.getSeller() != null ? domain.getSeller().getIdentifier() : null;
        String productType = (domain instanceof DigitalProduct) ? ProductType.DIGITAL.name() : ProductType.PHYSICAL.name();
        ProductJpaEntity.ProductJpaEntityBuilder builder = ProductJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .name(domain.getName())
                .description(domain.getDescription())
                .sellerIdentifier(sellerId)
                .status(domain.getStatus() != null ? domain.getStatus().name() : ProductStatus.SUSPENDED.name())
                .productType(productType);

        if (domain instanceof PhysicalProduct physical) {
            builder.weight(physical.getWeight());
        } else if (domain instanceof DigitalProduct digital) {
            builder.deliveryAsset(digital.getDeliveryAsset());
        }

        return builder.build();
    }

    public static Product toDomain(ProductJpaEntity entity) {
        if (entity == null) return null;
        Product product;
        if ("DIGITAL".equalsIgnoreCase(entity.getProductType())) {
            DigitalProduct digital = new DigitalProduct();
            digital.setDeliveryAsset(entity.getDeliveryAsset());
            digital.setProductType(ProductType.DIGITAL);
            product = digital;
        } else {
            PhysicalProduct physical = new PhysicalProduct();
            physical.setWeight(entity.getWeight());
            physical.setProductType(ProductType.PHYSICAL);
            product = physical;
        }
        product.setIdentifier(entity.getIdentifier());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        if (entity.getStatus() != null) {
            try {
                product.setStatus(ProductStatus.valueOf(entity.getStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getSellerIdentifier() != null) {
            Seller seller = new Seller();
            seller.setIdentifier(entity.getSellerIdentifier());
            product.setSeller(seller);
        }
        return product;
    }
}
