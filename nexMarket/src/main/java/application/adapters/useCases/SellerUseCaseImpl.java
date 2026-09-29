package application.adapters.useCases;

import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.models.Warehouse;
import application.domain.ports.in.SellerPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.services.catalog.ConsultProductService;
import application.domain.services.catalog.DiscontinueProductService;
import application.domain.services.catalog.PublishProductService;
import application.domain.services.catalog.RegisterProductService;
import application.domain.services.catalog.SuspendProductService;
import application.domain.services.inventory.ConsultInventoryService;
import application.domain.services.seller.ConsultSellerProductsService;
import application.domain.services.seller.ConsultSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerUseCaseImpl implements SellerPort {

    private final ConsultSellerService consultSellerService;
    private final RegisterProductService registerProductService;
    private final ConsultProductService consultProductService;
    private final PublishProductService publishProductService;
    private final SuspendProductService suspendProductService;
    private final DiscontinueProductService discontinueProductService;
    private final ConsultSellerProductsService consultSellerProductsService;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final ConsultInventoryService consultInventoryService;

    @Override
    public Seller consultMyProfile(User user) {
        Seller target = new Seller();
        target.setIdentifier(user.getIdentifier());
        return consultSellerService.getByIdentifier(target);
    }

    @Override
    public Product registerProduct(User user, Product product) {
        Seller seller = consultMyProfile(user);
        return registerProductService.registerProduct(product, seller);
    }

    @Override
    public Product updateProduct(User user, Product product) {
        Product existing = consultProductService.getByIdentifier(product);
        if (product.getName() != null) existing.setName(product.getName());
        if (product.getDescription() != null) existing.setDescription(product.getDescription());
        productRepositoryPort.update(existing);
        return existing;
    }

    @Override
    public void publishProduct(User user, Product product) {
        publishProductService.publishProduct(product);
    }

    @Override
    public void suspendProduct(User user, Product product) {
        suspendProductService.suspendProduct(product);
    }

    @Override
    public void discontinueProduct(User user, Product product) {
        discontinueProductService.discontinueProduct(product);
    }

    @Override
    public List<Product> consultMyProducts(User user) {
        Seller seller = consultMyProfile(user);
        return consultSellerProductsService.getProductsBySeller(seller);
    }

    @Override
    public List<Warehouse> consultMyWarehouses(User user) {
        Seller seller = consultMyProfile(user);
        return warehouseRepositoryPort.findBySeller(seller);
    }

    @Override
    public List<Inventory> consultMyInventory(User user, Warehouse warehouse) {
        return consultInventoryService.getByWarehouse(warehouse);
    }
}
