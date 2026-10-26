package application.adapters.persistence.jpa.adapters;

import application.domain.models.Seller;
import application.domain.models.Warehouse;
import application.domain.ports.out.WarehouseRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class WarehouseJpaAdapter implements WarehouseRepositoryPort {

    private final Map<String, Warehouse> store = new ConcurrentHashMap<>();

    @Override
    public Warehouse save(Warehouse warehouse) {
        if (warehouse == null || warehouse.getIdentifier() == null) return warehouse;
        store.put(warehouse.getIdentifier(), warehouse);
        return warehouse;
    }

    @Override
    public Optional<Warehouse> findByIdentifier(Warehouse warehouse) {
        if (warehouse == null || warehouse.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(warehouse.getIdentifier()));
    }

    @Override
    public List<Warehouse> findBySeller(Seller seller) {
        return List.copyOf(store.values());
    }

    @Override
    public List<Warehouse> findByType(Warehouse warehouse) {
        if (warehouse == null || warehouse.getType() == null) {
            return List.copyOf(store.values());
        }
        return store.values().stream()
                .filter(w -> warehouse.getType().equals(w.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Warehouse warehouse) {
        if (warehouse != null && warehouse.getIdentifier() != null) {
            store.put(warehouse.getIdentifier(), warehouse);
        }
    }
}
