package application.domain.services.inventory;

import application.domain.models.Inventory;
import application.domain.models.Product;
import application.domain.models.Warehouse;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.InventoryRepositoryPort;

import java.util.List;

public class ConsultInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public ConsultInventoryService(InventoryRepositoryPort inventoryRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    public Inventory getByProductAndWarehouse(Inventory query) {
        return inventoryRepositoryPort.findByProductAndWarehouse(query)
                .orElseThrow(() -> new EntityNotFoundException("Registro de inventario no encontrado para el producto y almacén especificados."));
    }

    public List<Inventory> getByProduct(Product product) {
        return inventoryRepositoryPort.findByProduct(product);
    }

    public List<Inventory> getByWarehouse(Warehouse warehouse) {
        return inventoryRepositoryPort.findByWarehouse(warehouse);
    }
}
