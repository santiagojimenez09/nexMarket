package application.domain.ports.in;

import application.domain.models.Buyer;
import application.domain.models.Order;

public interface ConfirmOrderUseCase {
    Order confirmOrder(Buyer buyer);
}
