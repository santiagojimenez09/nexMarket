package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.WarehouseJpaEntity;
import application.adapters.persistence.jpa.mappers.WarehouseJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaWarehouseRepository;
import application.domain.models.Seller;
import application.domain.models.Warehouse;
import application.domain.ports.out.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WarehouseJpaAdapter implements WarehouseRepositoryPort {

    private final SpringDataJpaWarehouseRepository warehouseRepository;

    @Override
    public Warehouse save(Warehouse warehouse) {
        if (warehouse == null) return null;
        WarehouseJpaEntity entity = WarehouseJpaMapper.toEntity(warehouse);
        WarehouseJpaEntity saved = warehouseRepository.save(entity);
        return WarehouseJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Warehouse> findByIdentifier(Warehouse warehouse) {
        if (warehouse == null || warehouse.getIdentifier() == null) return Optional.empty();
        return warehouseRepository.findById(warehouse.getIdentifier()).map(WarehouseJpaMapper::toDomain);
    }

    @Override
    public List<Warehouse> findBySeller(Seller seller) {
        if (seller == null || seller.getIdentifier() == null) {
            return warehouseRepository.findAll().stream().map(WarehouseJpaMapper::toDomain).collect(Collectors.toList());
        }
        return warehouseRepository.findByOwnerIdentifier(seller.getIdentifier()).stream()
                .map(WarehouseJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Warehouse> findByType(Warehouse warehouse) {
        if (warehouse == null || warehouse.getType() == null) {
            return warehouseRepository.findAll().stream().map(WarehouseJpaMapper::toDomain).collect(Collectors.toList());
        }
        return warehouseRepository.findByType(warehouse.getType().name()).stream()
                .map(WarehouseJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Warehouse warehouse) {
        if (warehouse != null && warehouse.getIdentifier() != null) {
            warehouseRepository.save(WarehouseJpaMapper.toEntity(warehouse));
        }
    }
}
