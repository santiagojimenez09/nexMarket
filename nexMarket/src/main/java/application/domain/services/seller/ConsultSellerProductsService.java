package application.domain.services.seller;

import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.SellerRepositoryPort;

import java.util.List;

public class ConsultSellerProductsService {

    private final SellerRepositoryPort sellerRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public ConsultSellerProductsService(SellerRepositoryPort sellerRepositoryPort,
                                        ProductRepositoryPort productRepositoryPort) {
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    public List<Product> getProductsBySeller(Seller seller) {
        sellerRepositoryPort.findByIdentifier(seller)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + seller.getIdentifier()));

        return productRepositoryPort.findBySeller(seller);
    }
}
