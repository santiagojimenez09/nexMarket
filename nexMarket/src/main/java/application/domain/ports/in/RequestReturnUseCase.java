package application.domain.ports.in;

import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.Return;

import java.util.List;

public interface RequestReturnUseCase {
    Return requestReturn(Order order, List<OrderItem> items, String reason);
}
