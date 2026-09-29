package application.domain.ports.in;

import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Seller;
import application.domain.models.User;
import application.domain.models.Warehouse;
import java.util.List;

public interface SellerPort {
    Seller consultMyProfile(User user);
    
    // Products & Catalog
    Product registerProduct(User user, Product product);
    Product updateProduct(User user, Product product);
    void publishProduct(User user, Product product);
    void suspendProduct(User user, Product product);
    void discontinueProduct(User user, Product product);
    List<Product> consultMyProducts(User user);
    
    // Warehouses & Stock
    List<Warehouse> consultMyWarehouses(User user);
    List<Inventory> consultMyInventory(User user, Warehouse warehouse);
}
