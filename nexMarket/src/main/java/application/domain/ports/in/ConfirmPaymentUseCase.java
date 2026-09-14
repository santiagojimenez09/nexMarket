package application.domain.ports.in;

import application.domain.models.Order;

public interface ConfirmPaymentUseCase {
    Order confirmPayment(Order order);
}
