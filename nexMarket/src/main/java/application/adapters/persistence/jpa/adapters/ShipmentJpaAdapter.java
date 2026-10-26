package application.adapters.persistence.jpa.adapters;

import application.domain.models.LogisticsOperator;
import application.domain.models.Order;
import application.domain.models.Shipment;
import application.domain.ports.out.ShipmentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ShipmentJpaAdapter implements ShipmentRepositoryPort {

    private final Map<String, Shipment> store = new ConcurrentHashMap<>();

    @Override
    public Shipment save(Shipment shipment) {
        if (shipment == null || shipment.getIdentifier() == null) return shipment;
        store.put(shipment.getIdentifier(), shipment);
        return shipment;
    }

    @Override
    public Optional<Shipment> findByIdentifier(Shipment shipment) {
        if (shipment == null || shipment.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(shipment.getIdentifier()));
    }

    @Override
    public List<Shipment> findByOrder(Order order) {
        if (order == null || order.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(s -> s.getOrder() != null && order.getIdentifier().equals(s.getOrder().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Shipment> findByLogisticsOperator(LogisticsOperator operator) {
        if (operator == null || operator.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(s -> s.getLogisticsOperator() != null && operator.getIdentifier().equals(s.getLogisticsOperator().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Shipment shipment) {
        if (shipment != null && shipment.getIdentifier() != null) {
            store.put(shipment.getIdentifier(), shipment);
        }
    }
}
