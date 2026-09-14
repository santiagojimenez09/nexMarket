package application.domain.ports.out;

import application.domain.models.Seller;
import application.domain.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepositoryPort {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findByIdentifier(Warehouse warehouse);
    List<Warehouse> findBySeller(Seller seller);
    List<Warehouse> findByType(Warehouse warehouse);
    void update(Warehouse warehouse);
}
