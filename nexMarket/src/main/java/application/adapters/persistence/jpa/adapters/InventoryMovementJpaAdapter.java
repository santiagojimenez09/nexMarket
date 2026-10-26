package application.adapters.persistence.jpa.adapters;

import application.domain.models.Inventory;
import application.domain.models.InventoryMovement;
import application.domain.models.Order;
import application.domain.ports.out.InventoryMovementRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class InventoryMovementJpaAdapter implements InventoryMovementRepositoryPort {

    private final List<InventoryMovement> store = new CopyOnWriteArrayList<>();

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        if (movement != null) {
            store.add(movement);
        }
        return movement;
    }

    @Override
    public List<InventoryMovement> findByInventory(Inventory inventory) {
        if (inventory == null) return List.copyOf(store);
        return store.stream()
                .filter(m -> m.getInventory() != null && m.getInventory().equals(inventory))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovement> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) return List.copyOf(store);
        return store.stream()
                .filter(m -> m.getRelatedOrder() != null && order.getIdentifier().equals(m.getRelatedOrder().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovement> findByType(InventoryMovement movement) {
        if (movement == null || movement.getMovementType() == null) return List.copyOf(store);
        return store.stream()
                .filter(m -> movement.getMovementType().equals(m.getMovementType()))
                .collect(Collectors.toList());
    }
}
