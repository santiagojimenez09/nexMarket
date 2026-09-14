package application.domain.services.warehouse;

import application.domain.models.Seller;
import application.domain.models.Warehouse;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.WarehouseRepositoryPort;

import java.util.List;

public class ConsultWarehouseService {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    public ConsultWarehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        this.warehouseRepositoryPort = warehouseRepositoryPort;
    }

    public Warehouse getByIdentifier(Warehouse warehouse) {
        return warehouseRepositoryPort.findByIdentifier(warehouse)
                .orElseThrow(() -> new EntityNotFoundException("Almacén no encontrado: " + warehouse.getIdentifier()));
    }

    public List<Warehouse> getBySeller(Seller seller) {
        return warehouseRepositoryPort.findBySeller(seller);
    }

    public List<Warehouse> getByType(Warehouse warehouse) {
        return warehouseRepositoryPort.findByType(warehouse);
    }
}
