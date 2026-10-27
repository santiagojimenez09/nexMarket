package application.adapters.persistence.jpa.adapters;

import application.adapters.persistence.jpa.entities.OrderJpaEntity;
import application.adapters.persistence.jpa.mappers.OrderJpaMapper;
import application.adapters.persistence.jpa.repositories.SpringDataJpaOrderRepository;
import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.ports.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderJpaAdapter implements OrderRepositoryPort {

    private final SpringDataJpaOrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        if (order == null) return null;
        OrderJpaEntity entity = OrderJpaMapper.toEntity(order);
        OrderJpaEntity saved = orderRepository.save(entity);
        return OrderJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findByIdentifier(Order order) {
        if (order == null || order.getIdentifier() == null) return Optional.empty();
        return orderRepository.findById(order.getIdentifier()).map(OrderJpaMapper::toDomain);
    }

    @Override
    public List<Order> findByBuyer(Buyer buyer) {
        if (buyer == null || buyer.getIdentifier() == null) {
            return orderRepository.findAll().stream().map(OrderJpaMapper::toDomain).collect(Collectors.toList());
        }
        return orderRepository.findByBuyerIdentifier(buyer.getIdentifier()).stream()
                .map(OrderJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(Order order) {
        if (order == null || order.getOrderStatus() == null) {
            return orderRepository.findAll().stream().map(OrderJpaMapper::toDomain).collect(Collectors.toList());
        }
        return orderRepository.findByOrderStatus(order.getOrderStatus().name()).stream()
                .map(OrderJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Order order) {
        if (order != null && order.getIdentifier() != null) {
            orderRepository.save(OrderJpaMapper.toEntity(order));
        }
    }
}
