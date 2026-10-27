package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.InventoryMovementJpaEntity;
import application.adapters.persistence.jpa.mappers.InventoryMovementJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaInventoryMovementRepository;
import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InventoryMovementJpaAdapter implements InventoryMovementRepositoryPort {

    private final SpringDataJpaInventoryMovementRepository movementRepository;

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        if (movement == null) return null;
        InventoryMovementJpaEntity entity = InventoryMovementJpaMapper.toEntity(movement);
        InventoryMovementJpaEntity saved = movementRepository.save(entity);
        return InventoryMovementJpaMapper.toDomain(saved);
    }

    @Override
    public List<InventoryMovement> findByInventory(Inventory inventory) {
        if (inventory == null) {
            return movementRepository.findAll().stream().map(InventoryMovementJpaMapper::toDomain).collect(Collectors.toList());
        }
        String whId = inventory.getWarehouse() != null ? inventory.getWarehouse().getIdentifier() : "";
        String prodId = inventory.getProduct() != null ? inventory.getProduct().getIdentifier() : "";
        String inventoryId = whId + ":" + prodId;
        return movementRepository.findByInventoryIdentifier(inventoryId).stream()
                .map(InventoryMovementJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovement> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) {
            return movementRepository.findAll().stream().map(InventoryMovementJpaMapper::toDomain).collect(Collectors.toList());
        }
        return movementRepository.findByRelatedOrderIdentifier(order.getIdentifier()).stream()
                .map(InventoryMovementJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovement> findByType(InventoryMovement movement) {
        if (movement == null || movement.getMovementType() == null) {
            return movementRepository.findAll().stream().map(InventoryMovementJpaMapper::toDomain).collect(Collectors.toList());
        }
        return movementRepository.findByMovementType(movement.getMovementType().name()).stream()
                .map(InventoryMovementJpaMapper::toDomain)
                .collect(Collectors.toList());
    }
}
