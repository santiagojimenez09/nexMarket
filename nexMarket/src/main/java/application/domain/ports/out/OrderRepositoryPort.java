package application.domain.ports.out;

import application.domain.models.Buyer;
import application.domain.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findByIdentifier(Order order);
    List<Order> findByBuyer(Buyer buyer);
    List<Order> findByStatus(Order order);
    void update(Order order);
}
