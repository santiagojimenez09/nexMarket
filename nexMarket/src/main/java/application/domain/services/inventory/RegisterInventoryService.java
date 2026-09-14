package application.domain.services.inventory;

import application.domain.models.Inventory;
import application.domain.models.PhysicalProduct;
import application.domain.models.Warehouse;
import application.domain.exceptions.DomainException;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueObjects.InventoryConditionStatus;

public class RegisterInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public RegisterInventoryService(InventoryRepositoryPort inventoryRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
    }

    public Inventory registerInventory(PhysicalProduct product, Warehouse warehouse, Integer initialStock) {
        if (product == null) {
            throw new DomainException("El producto físico es obligatorio para registrar inventario.");
        }
        if (warehouse == null) {
            throw new DomainException("El almacén es obligatorio para registrar inventario.");
        }
        if (initialStock == null || initialStock < 0) {
            throw new DomainException("El stock inicial no puede ser negativo.");
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setAvailableQuantity(initialStock);
        inventory.setReservedQuantity(0);
        inventory.setConditionStatus(InventoryConditionStatus.AVAILABLE);

        return inventoryRepositoryPort.save(inventory);
    }
}
