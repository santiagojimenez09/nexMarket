package application.domain.services.warehouse;

import application.domain.models.Warehouse;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueObjects.WarehouseStatus;

public class ChangeWarehouseStatusService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public ChangeWarehouseStatusService(WarehouseRepositoryPort warehouseRepositoryPort) {
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public void changeStatus(Warehouse warehouse, WarehouseStatus newStatus) {
        if (newStatus == null) {
            throw new InvalidStatusTransitionException("El nuevo estado del almacén no puede ser nulo.");
        }

        Warehouse existing = warehouseRepositoryPort.findByIdentifier(warehouse)
                .orElseThrow(() -> new EntityNotFoundException("Almacén no encontrado: " + warehouse.getIdentifier()));

        existing.setStatus(newStatus);
        warehouseRepositoryPort.update(existing);
    }
}
