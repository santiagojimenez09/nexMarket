package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.WarehouseJpaEntity;
import application.domain.models.Seller;
import application.domain.models.Warehouse;
import application.domain.valueObjects.WarehouseStatus;
import application.domain.valueObjects.WarehouseType;

public class WarehouseJpaMapper {

    public static WarehouseJpaEntity toEntity(Warehouse domain) {
        if (domain == null) return null;
        String ownerId = domain.getOwner() != null ? domain.getOwner().getIdentifier() : null;
        return WarehouseJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .name(domain.getName())
                .address(domain.getAddress())
                .type(domain.getType() != null ? domain.getType().name() : WarehouseType.SELLER.name())
                .status(domain.getStatus() != null ? domain.getStatus().name() : WarehouseStatus.ACTIVE.name())
                .ownerIdentifier(ownerId)
                .build();
    }

    public static Warehouse toDomain(WarehouseJpaEntity entity) {
        if (entity == null) return null;
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier(entity.getIdentifier());
        warehouse.setName(entity.getName());
        warehouse.setAddress(entity.getAddress());
        if (entity.getType() != null) {
            try {
                warehouse.setType(WarehouseType.valueOf(entity.getType()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getStatus() != null) {
            try {
                warehouse.setStatus(WarehouseStatus.valueOf(entity.getStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getOwnerIdentifier() != null) {
            Seller seller = new Seller();
            seller.setIdentifier(entity.getOwnerIdentifier());
            warehouse.setOwner(seller);
        }
        return warehouse;
    }
}
