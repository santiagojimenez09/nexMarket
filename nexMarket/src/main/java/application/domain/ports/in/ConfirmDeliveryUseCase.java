package application.domain.ports.in;

import application.domain.models.Order;

public interface ConfirmDeliveryUseCase {
    Order confirmDelivery(Order order);
}
