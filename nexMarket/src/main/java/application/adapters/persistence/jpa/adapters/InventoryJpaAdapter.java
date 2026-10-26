package application.adapters.persistence.jpa.adapters;

import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Warehouse;
import application.domain.ports.out.InventoryRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InventoryJpaAdapter implements InventoryRepositoryPort {

    private final Map<String, Inventory> store = new ConcurrentHashMap<>();

    private String buildKey(Inventory inventory) {
        if (inventory == null) return "";
        String prodId = inventory.getProduct() != null ? inventory.getProduct().getIdentifier() : "";
        String whId = inventory.getWarehouse() != null ? inventory.getWarehouse().getIdentifier() : "";
        return prodId + ":" + whId;
    }

    @Override
    public Inventory save(Inventory inventory) {
        if (inventory == null) return null;
        store.put(buildKey(inventory), inventory);
        return inventory;
    }

    @Override
    public Optional<Inventory> findByProductAndWarehouse(Inventory inventory) {
        if (inventory == null) return Optional.empty();
        return Optional.ofNullable(store.get(buildKey(inventory)));
    }

    @Override
    public List<Inventory> findByProduct(Product product) {
        if (product == null || product.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(i -> i.getProduct() != null && product.getIdentifier().equals(i.getProduct().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventory> findByWarehouse(Warehouse warehouse) {
        if (warehouse == null || warehouse.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(i -> i.getWarehouse() != null && warehouse.getIdentifier().equals(i.getWarehouse().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Inventory inventory) {
        if (inventory != null) {
            store.put(buildKey(inventory), inventory);
        }
    }
}
