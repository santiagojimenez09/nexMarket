package application.domain.models;

import application.domain.valueObjects.WarehouseStatus;
import application.domain.valueObjects.WarehouseType;

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
