package application.domain.services.order;

import application.domain.models.Order;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.OrderRepositoryPort;

import java.util.List;

public class ConsultOrderService {

    private final OrderRepositoryPort orderRepositoryPort;

    public ConsultOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public Order getByIdentifier(Order order) {
        return orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));
    }

    public List<Order> getByStatus(Order order) {
        return orderRepositoryPort.findByStatus(order);
    }
}
