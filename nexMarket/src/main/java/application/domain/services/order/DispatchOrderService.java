package application.domain.services.order;

import application.domain.models.Order;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.ports.in.DispatchOrderUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

public class DispatchOrderService implements DispatchOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public DispatchOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public Order dispatchOrder(Order order) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Un pedido en estado ENTREGADO no puede ser modificado.");
        }

        if (existingOrder.getOrderStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStatusException("Solo pedidos en estado PAGADO pueden ser despachados. Estado actual: " + existingOrder.getOrderStatus());
        }

        existingOrder.setOrderStatus(OrderStatus.DISPATCHED);
        orderRepositoryPort.update(existingOrder);
        return existingOrder;
    }
}
