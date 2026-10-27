package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.InventoryMovementJpaEntity;
import application.domain.models.*;
import application.domain.valueObjects.InventoryMovementType;

public class InventoryMovementJpaMapper {

    public static InventoryMovementJpaEntity toEntity(InventoryMovement domain) {
        if (domain == null) return null;
        String inventoryId = null;
        if (domain.getInventory() != null) {
            String whId = domain.getInventory().getWarehouse() != null ? domain.getInventory().getWarehouse().getIdentifier() : "";
            String prodId = domain.getInventory().getProduct() != null ? domain.getInventory().getProduct().getIdentifier() : "";
            inventoryId = whId + ":" + prodId;
        }
        String orderId = domain.getRelatedOrder() != null ? domain.getRelatedOrder().getIdentifier() : null;
        String userId = domain.getPerformedBy() != null ? domain.getPerformedBy().getIdentifier() : null;

        return InventoryMovementJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .inventoryIdentifier(inventoryId)
                .movementType(domain.getMovementType() != null ? domain.getMovementType().name() : InventoryMovementType.INBOUND.name())
                .quantity(domain.getQuantity() != null ? domain.getQuantity() : 0)
                .relatedOrderIdentifier(orderId)
                .performedByIdentifier(userId)
                .movementDate(domain.getMovementDate())
                .build();
    }

    public static InventoryMovement toDomain(InventoryMovementJpaEntity entity) {
        if (entity == null) return null;
        InventoryMovement movement = new InventoryMovement();
        movement.setIdentifier(entity.getIdentifier());
        movement.setQuantity(entity.getQuantity());
        movement.setMovementDate(entity.getMovementDate());
        if (entity.getMovementType() != null) {
            try {
                movement.setMovementType(InventoryMovementType.valueOf(entity.getMovementType()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getInventoryIdentifier() != null) {
            Inventory inv = new Inventory();
            String[] parts = entity.getInventoryIdentifier().split(":");
            if (parts.length > 0 && !parts[0].isEmpty()) {
                Warehouse wh = new Warehouse();
                wh.setIdentifier(parts[0]);
                inv.setWarehouse(wh);
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                PhysicalProduct p = new PhysicalProduct();
                p.setIdentifier(parts[1]);
                inv.setProduct(p);
            }
            movement.setInventory(inv);
        }
        if (entity.getRelatedOrderIdentifier() != null) {
            Order order = new Order();
            order.setIdentifier(entity.getRelatedOrderIdentifier());
            movement.setRelatedOrder(order);
        }
        if (entity.getPerformedByIdentifier() != null) {
            Administrator admin = new Administrator();
            admin.setIdentifier(entity.getPerformedByIdentifier());
            movement.setPerformedBy(admin);
        }
        return movement;
    }
}
