package application.domain.services.order;

import application.domain.models.Order;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.ports.in.ConfirmDeliveryUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

public class ConfirmDeliveryService implements ConfirmDeliveryUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public ConfirmDeliveryService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public Order confirmDelivery(Order order) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("El pedido ya se encuentra en estado ENTREGADO y no puede ser modificado.");
        }

        if (existingOrder.getOrderStatus() != OrderStatus.DISPATCHED) {
            throw new InvalidOrderStatusException("Solo pedidos en estado DESPACHADO pueden pasar a ENTREGADO. Estado actual: " + existingOrder.getOrderStatus());
        }

        existingOrder.setOrderStatus(OrderStatus.DELIVERED);
        orderRepositoryPort.update(existingOrder);
        return existingOrder;
    }
}
