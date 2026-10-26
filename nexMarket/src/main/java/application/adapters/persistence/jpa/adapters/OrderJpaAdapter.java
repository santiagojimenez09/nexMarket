package application.adapters.persistence.jpa.adapters;

import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.ports.out.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderJpaAdapter implements OrderRepositoryPort {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        if (order == null || order.getIdentifier() == null) return order;
        store.put(order.getIdentifier(), order);
        return order;
    }

    @Override
    public Optional<Order> findByIdentifier(Order order) {
        if (order == null || order.getIdentifier() == null) return Optional.empty();
        return Optional.ofNullable(store.get(order.getIdentifier()));
    }

    @Override
    public List<Order> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(o -> o.getBuyer() != null && buyer.getIdentifier().equals(o.getBuyer().getIdentifier()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(Order order) {
        if (order == null || order.getOrderStatus() == null) return List.copyOf(store.values());
        return store.values().stream()
                .filter(o -> order.getOrderStatus().equals(o.getOrderStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Order order) {
        if (order != null && order.getIdentifier() != null) {
            store.put(order.getIdentifier(), order);
        }
    }
}
