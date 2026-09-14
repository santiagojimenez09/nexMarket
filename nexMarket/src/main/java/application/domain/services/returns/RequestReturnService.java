package application.domain.services.returns;

import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.Return;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidOrderStatusException;
import application.domain.ports.in.RequestReturnUseCase;
import application.domain.ports.out.BusinessConfigurationPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.OrderStatus;
import application.domain.valueObjects.ReturnStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RequestReturnService implements RequestReturnUseCase {

    private final ReturnRepositoryPort returnRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final BusinessConfigurationPort businessConfigurationPort;

    public RequestReturnService(ReturnRepositoryPort returnRepositoryPort,
                                OrderRepositoryPort orderRepositoryPort,
                                BusinessConfigurationPort businessConfigurationPort) {
        this.returnRepositoryPort = returnRepositoryPort;
        this.orderRepositoryPort = orderRepositoryPort;
        this.businessConfigurationPort = businessConfigurationPort;
    }

    public Return requestReturn(Order order, List<OrderItem> items, String reason) {
        Order existingOrder = orderRepositoryPort.findByIdentifier(order)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + order.getIdentifier()));

        if (existingOrder.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Solo se pueden solicitar devoluciones para pedidos en estado ENTREGADO.");
        }

        if (items == null || items.isEmpty()) {
            throw new DomainException("Debe especificar al menos un ítem a devolver.");
        }

        Integer returnWindowDays = businessConfigurationPort.getReturnWindowDays();
        if (returnWindowDays != null && existingOrder.getCreationDate() != null) {
            LocalDateTime deadline = existingOrder.getCreationDate().plusDays(returnWindowDays);
            if (LocalDateTime.now().isAfter(deadline)) {
                throw new DomainException("El plazo límite de " + returnWindowDays + " días para devoluciones ha expirado.");
            }
        }

        Return returnRequest = new Return();
        returnRequest.setIdentifier("RET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        returnRequest.setOrder(existingOrder);
        returnRequest.setItems(items);
        returnRequest.setReason(reason);
        returnRequest.setReturnStatus(ReturnStatus.REQUESTED);
        returnRequest.setRequestDate(LocalDateTime.now());

        return returnRepositoryPort.save(returnRequest);
    }
}
