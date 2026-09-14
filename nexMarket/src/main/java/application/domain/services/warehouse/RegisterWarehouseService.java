package application.domain.services.warehouse;

import application.domain.models.Warehouse;
import application.domain.exceptions.DomainException;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueObjects.WarehouseStatus;
import application.domain.valueObjects.WarehouseType;

public class RegisterWarehouseService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public RegisterWarehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Warehouse registerWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new DomainException("El almacén no puede ser nulo.");
        }
        if (warehouse.getIdentifier() == null || warehouse.getIdentifier().trim().isEmpty()) {
            throw new DomainException("El identificador del almacén es obligatorio.");
        }
        if (warehouse.getType() == null) {
            throw new DomainException("El tipo de almacén es obligatorio.");
        }

        // Business rule: SELLER warehouses must have an owner, MARKETPLACE must not.
        if (warehouse.getType() == WarehouseType.SELLER && warehouse.getOwner() == null) {
            throw new DomainException("Un almacén de tipo SELLER debe tener un vendedor propietario asignado.");
        }
        if (warehouse.getType() == WarehouseType.MARKETPLACE && warehouse.getOwner() != null) {
            throw new DomainException("Un almacén de tipo MARKETPLACE no debe tener propietario asignado.");
        }

        if (warehouse.getStatus() == null) {
            warehouse.setStatus(WarehouseStatus.ACTIVE);
        }

        return warehouseRepositoryPort.save(warehouse);
    }
}
