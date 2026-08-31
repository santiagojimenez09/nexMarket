package application.domains;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Seller extends User {
    private Administrator registeredBy;
    private List<Warehouse> warehouses = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
}
