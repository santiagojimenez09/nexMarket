package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.OrderJpaEntity;
import application.adapters.persistence.jpa.entities.OrderItemEmbeddable;
import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.PhysicalProduct;
import application.domain.valueObjects.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderJpaMapper {

    public static OrderJpaEntity toEntity(Order domain) {
        if (domain == null) return null;
        String buyerId = domain.getBuyer() != null ? domain.getBuyer().getIdentifier() : null;

        List<OrderItemEmbeddable> itemEntities = new ArrayList<>();
        if (domain.getItems() != null) {
            itemEntities = domain.getItems().stream()
                    .filter(i -> i != null && i.getProduct() != null)
                    .map(i -> OrderItemEmbeddable.builder()
                            .productIdentifier(i.getProduct().getIdentifier())
                            .quantity(i.getQuantity())
                            .unitPrice(i.getUnitPrice())
                            .build())
                    .collect(Collectors.toList());
        }

        return OrderJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .buyerIdentifier(buyerId)
                .orderStatus(domain.getOrderStatus() != null ? domain.getOrderStatus().name() : OrderStatus.PENDING_PAYMENT.name())
                .creationDate(domain.getCreationDate())
                .items(itemEntities)
                .build();
    }

    public static Order toDomain(OrderJpaEntity entity) {
        if (entity == null) return null;
        Order order = new Order();
        order.setIdentifier(entity.getIdentifier());
        order.setCreationDate(entity.getCreationDate());

        if (entity.getOrderStatus() != null) {
            try {
                order.setOrderStatus(OrderStatus.valueOf(entity.getOrderStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (entity.getBuyerIdentifier() != null) {
            Buyer buyer = new Buyer();
            buyer.setIdentifier(entity.getBuyerIdentifier());
            order.setBuyer(buyer);
        }

        List<OrderItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(i -> {
                        PhysicalProduct p = new PhysicalProduct();
                        p.setIdentifier(i.getProductIdentifier());
                        return new OrderItem(p, i.getQuantity(), i.getUnitPrice());
                    })
                    .collect(Collectors.toList());
        }
        order.setItems(items);
        return order;
    }
}
