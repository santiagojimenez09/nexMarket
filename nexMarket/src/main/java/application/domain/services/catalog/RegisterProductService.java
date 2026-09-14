package application.domain.services.catalog;

import application.domain.models.DigitalProduct;
import application.domain.models.PhysicalProduct;
import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.in.RegisterProductUseCase;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueObjects.ProductStatus;
import application.domain.valueObjects.ProductType;

public class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;

    public RegisterProductService(ProductRepositoryPort productRepositoryPort,
                                  SellerRepositoryPort sellerRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.sellerRepositoryPort = sellerRepositoryPort;
    }

    public Product registerProduct(Product product, Seller seller) {
        if (product == null) {
            throw new DomainException("El producto no puede ser nulo.");
        }
        if (seller == null || seller.getIdentifier() == null) {
            throw new DomainException("El vendedor responsable es obligatorio.");
        }

        Seller existingSeller = sellerRepositoryPort.findByIdentifier(seller)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + seller.getIdentifier()));

        product.setSeller(existingSeller);
        product.setStatus(ProductStatus.SUSPENDED); // Default status prior to publication

        if (product instanceof PhysicalProduct physical) {
            product.setProductType(ProductType.PHYSICAL);
            if (physical.getWeight() == null) {
                throw new DomainException("El peso es obligatorio para productos físicos.");
            }
        } else if (product instanceof DigitalProduct digital) {
            product.setProductType(ProductType.DIGITAL);
            if (digital.getDeliveryAsset() == null || digital.getDeliveryAsset().trim().isEmpty()) {
                throw new DomainException("El activo de descarga es obligatorio para productos digitales.");
            }
        }

        return productRepositoryPort.save(product);
    }
}
