package application.domain.ports.out;

import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {
    Inventory save(Inventory inventory);
    Optional<Inventory> findByProductAndWarehouse(Inventory inventory);
    List<Inventory> findByProduct(Product product);
    List<Inventory> findByWarehouse(Warehouse warehouse);
    void update(Inventory inventory);
}
