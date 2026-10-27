package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.InventoryJpaEntity;
import application.domain.models.Inventory;
import application.domain.models.PhysicalProduct;
import application.domain.models.Warehouse;
import application.domain.valueObjects.InventoryConditionStatus;

public class InventoryJpaMapper {

    public static InventoryJpaEntity toEntity(Inventory domain) {
        if (domain == null) return null;
        String warehouseId = domain.getWarehouse() != null ? domain.getWarehouse().getIdentifier() : "";
        String productId = domain.getProduct() != null ? domain.getProduct().getIdentifier() : "";
        String id = warehouseId + ":" + productId;

        return InventoryJpaEntity.builder()
                .identifier(id)
                .warehouseIdentifier(warehouseId)
                .productIdentifier(productId)
                .availableQuantity(domain.getAvailableQuantity() != null ? domain.getAvailableQuantity() : 0)
                .reservedQuantity(domain.getReservedQuantity() != null ? domain.getReservedQuantity() : 0)
                .conditionStatus(domain.getConditionStatus() != null ? domain.getConditionStatus().name() : null)
                .build();
    }

    public static Inventory toDomain(InventoryJpaEntity entity) {
        if (entity == null) return null;
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(entity.getAvailableQuantity());
        inventory.setReservedQuantity(entity.getReservedQuantity());
        if (entity.getConditionStatus() != null) {
            try {
                inventory.setConditionStatus(InventoryConditionStatus.valueOf(entity.getConditionStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getWarehouseIdentifier() != null) {
            Warehouse warehouse = new Warehouse();
            warehouse.setIdentifier(entity.getWarehouseIdentifier());
            inventory.setWarehouse(warehouse);
        }
        if (entity.getProductIdentifier() != null) {
            PhysicalProduct product = new PhysicalProduct();
            product.setIdentifier(entity.getProductIdentifier());
            inventory.setProduct(product);
        }
        return inventory;
    }
}
