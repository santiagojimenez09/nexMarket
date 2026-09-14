package application.domain.models;

import application.domain.valueObjects.InventoryConditionStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Inventory {
    private PhysicalProduct product;
    private Warehouse warehouse;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private InventoryConditionStatus conditionStatus;
}
