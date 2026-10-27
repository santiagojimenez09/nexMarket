package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.InventoryJpaEntity;
import application.adapters.persistence.jpa.mappers.InventoryJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaInventoryRepository;
import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Warehouse;
import application.domain.ports.out.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InventoryJpaAdapter implements InventoryRepositoryPort {

    private final SpringDataJpaInventoryRepository inventoryRepository;

    @Override
    public Inventory save(Inventory inventory) {
        if (inventory == null) return null;
        InventoryJpaEntity entity = InventoryJpaMapper.toEntity(inventory);
        InventoryJpaEntity saved = inventoryRepository.save(entity);
        return InventoryJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Inventory> findByProductAndWarehouse(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getWarehouse() == null) {
            return Optional.empty();
        }
        return inventoryRepository.findByProductIdentifierAndWarehouseIdentifier(
                inventory.getProduct().getIdentifier(),
                inventory.getWarehouse().getIdentifier()
        ).map(InventoryJpaMapper::toDomain);
    }

    @Override
    public List<Inventory> findByProduct(Product product) {
        if (product == null || product.getIdentifier() == null) {
            return inventoryRepository.findAll().stream().map(InventoryJpaMapper::toDomain).collect(Collectors.toList());
        }
        return inventoryRepository.findByProductIdentifier(product.getIdentifier()).stream()
                .map(InventoryJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventory> findByWarehouse(Warehouse warehouse) {
        if (warehouse == null || warehouse.getIdentifier() == null) {
            return inventoryRepository.findAll().stream().map(InventoryJpaMapper::toDomain).collect(Collectors.toList());
        }
        return inventoryRepository.findByWarehouseIdentifier(warehouse.getIdentifier()).stream()
                .map(InventoryJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Inventory inventory) {
        if (inventory != null) {
            inventoryRepository.save(InventoryJpaMapper.toEntity(inventory));
        }
    }
}
