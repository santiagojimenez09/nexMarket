package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.ShipmentJpaEntity;
import application.adapters.persistence.jpa.mappers.ShipmentJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaShipmentRepository;
import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.ports.out.ShipmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShipmentJpaAdapter implements ShipmentRepositoryPort {

    private final SpringDataJpaShipmentRepository shipmentRepository;

    @Override
    public Shipment save(Shipment shipment) {
        if (shipment == null) return null;
        ShipmentJpaEntity entity = ShipmentJpaMapper.toEntity(shipment);
        ShipmentJpaEntity saved = shipmentRepository.save(entity);
        return ShipmentJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Shipment> findByIdentifier(Shipment shipment) {
        if (shipment == null || shipment.getIdentifier() == null) return Optional.empty();
        return shipmentRepository.findById(shipment.getIdentifier()).map(ShipmentJpaMapper::toDomain);
    }

    @Override
    public List<Shipment> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) {
            return shipmentRepository.findAll().stream().map(ShipmentJpaMapper::toDomain).collect(Collectors.toList());
        }
        return shipmentRepository.findByOrderIdentifier(order.getIdentifier()).stream()
                .map(ShipmentJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Shipment> findByLogisticsOperator(LogisticsOperator operator) {
        if (operator == null || operator.getIdentifier() == null) {
            return shipmentRepository.findAll().stream().map(ShipmentJpaMapper::toDomain).collect(Collectors.toList());
        }
        return shipmentRepository.findByLogisticsOperatorIdentifier(operator.getIdentifier()).stream()
                .map(ShipmentJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Shipment shipment) {
        if (shipment != null && shipment.getIdentifier() != null) {
            shipmentRepository.save(ShipmentJpaMapper.toEntity(shipment));
        }
    }
}
