package application.domains;

import application.valueObjects.WarehouseStatus;
import application.valueObjects.WarehouseType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Warehouse {
    private String identifier;
    private String name;
    private String address;
    private WarehouseType type;
    private Seller owner;
    private WarehouseStatus status;
}
