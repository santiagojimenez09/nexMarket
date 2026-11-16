package application.adapters.useCases;

import application.domain.models.Inventory;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.Product;
import application.domain.models.Return;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.models.Warehouse;
import application.domain.ports.in.SellerPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final ReturnRepositoryPort returnRepositoryPort;

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

    @Override
    public Map<String, Object> generateSellerReport(User user) {
        Seller seller = consultMyProfile(user);
        List<Product> products = consultSellerProductsService.getProductsBySeller(seller);
        List<String> productIds = products.stream().map(Product::getIdentifier).toList();

        Map<String, Object> report = new HashMap<>();
        report.put("sellerIdentifier", seller.getIdentifier());
        report.put("sellerName", seller.getFullName());
        report.put("totalProducts", products.size());

        List<Map<String, Object>> productDetails = new ArrayList<>();
        int totalAvailableStock = 0;
        for (Product p : products) {
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("identifier", p.getIdentifier());
            pMap.put("name", p.getName());
            pMap.put("type", p.getProductType() != null ? p.getProductType().name() : null);
            pMap.put("status", p.getStatus() != null ? p.getStatus().name() : null);

            List<Inventory> inventories = inventoryRepositoryPort.findByProduct(p);
            int stock = inventories.stream().mapToInt(Inventory::getAvailableQuantity).sum();
            pMap.put("availableInventory", stock);
            totalAvailableStock += stock;
            productDetails.add(pMap);
        }
        report.put("products", productDetails);
        report.put("totalAvailableInventory", totalAvailableStock);

        // Sales analysis
        List<Order> allOrders = orderRepositoryPort.findByStatus(null);
        int totalUnitsSold = 0;
        BigDecimal totalSalesAmount = BigDecimal.ZERO;
        int ordersCount = 0;

        for (Order o : allOrders) {
            boolean sellerInvolved = false;
            if (o.getItems() != null) {
                for (OrderItem item : o.getItems()) {
                    if (item.getProduct() != null && productIds.contains(item.getProduct().getIdentifier())) {
                        sellerInvolved = true;
                        totalUnitsSold += item.getQuantity();
                        if (item.getUnitPrice() != null) {
                            totalSalesAmount = totalSalesAmount.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                        }
                    }
                }
            }
            if (sellerInvolved) ordersCount++;
        }

        Map<String, Object> salesSummary = new HashMap<>();
        salesSummary.put("totalUnitsSold", totalUnitsSold);
        salesSummary.put("totalSalesAmount", totalSalesAmount);
        salesSummary.put("ordersInvolved", ordersCount);
        report.put("salesSummary", salesSummary);

        // Returns analysis
        List<Return> allReturns = returnRepositoryPort.findByStatus(null);
        int returnedUnits = 0;
        int returnsCount = 0;
        for (Return ret : allReturns) {
            if (ret.getItems() != null) {
                for (OrderItem ri : ret.getItems()) {
                    if (ri.getProduct() != null && productIds.contains(ri.getProduct().getIdentifier())) {
                        returnsCount++;
                        returnedUnits += ri.getQuantity();
                    }
                }
            }
        }
        Map<String, Object> returnsSummary = new HashMap<>();
        returnsSummary.put("totalReturns", returnsCount);
        returnsSummary.put("totalUnitsReturned", returnedUnits);
        report.put("returnsSummary", returnsSummary);

        return report;
    }
}
