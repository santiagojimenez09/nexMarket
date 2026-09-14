package application.domain.services.order;

import application.domain.models.Order;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.ports.in.ConfirmPaymentUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.valueObjects.OrderStatus;

public class ConfirmPaymentService implements ConfirmPaymentUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public ConfirmPaymentService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public Order confirmPayment(Order order) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Un pedido en estado ENTREGADO no puede ser modificado.");
        }

        if (existingOrder.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new InvalidOrderStatusException("Solo pedidos en estado PENDIENTE_DE_PAGO pueden ser marcados como PAGADOS. Estado actual: " + existingOrder.getOrderStatus());
        }

        existingOrder.setOrderStatus(OrderStatus.PAID);
        orderRepositoryPort.update(existingOrder);
        return existingOrder;
    }
}
