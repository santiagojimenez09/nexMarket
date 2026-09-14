package application.domain.services.order;

import application.domain.models.Buyer;
import application.domain.models.CartItem;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.ShoppingCart;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.UserNotEligibleException;
import application.domain.ports.in.ConfirmOrderUseCase;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ShoppingCartRepositoryPort;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ConfirmOrderService implements ConfirmOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ShoppingCartRepositoryPort shoppingCartRepositoryPort;

    public ConfirmOrderService(OrderRepositoryPort orderRepositoryPort,
                               ShoppingCartRepositoryPort shoppingCartRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.shoppingCartRepositoryPort = shoppingCartRepositoryPort;
    }

    public Order confirmOrder(Buyer buyer) {
        if (buyer.getCommercialStatus() != BuyerCommercialStatus.ACTIVE) {
            throw new UserNotEligibleException("El comprador no tiene estado comercial ACTIVO para realizar pedidos.");
        }

        ShoppingCart cart = shoppingCartRepositoryPort.findByBuyer(buyer)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró carrito de compras para el comprador."));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new DomainException("No se puede generar un pedido a partir de un carrito vacío.");
        }

        Order order = new Order();
        order.setIdentifier(UUID.randomUUID().toString());
        order.setBuyer(buyer);
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreationDate(LocalDateTime.now());

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(100.0)); // Precio fijado al momento de compra
            order.getItems().add(orderItem);
        }

        Order savedOrder = orderRepositoryPort.save(order);

        // Clear cart after checkout
        cart.getItems().clear();
        cart.setLastUpdated(LocalDateTime.now());
        shoppingCartRepositoryPort.update(cart);

        return savedOrder;
    }
}
