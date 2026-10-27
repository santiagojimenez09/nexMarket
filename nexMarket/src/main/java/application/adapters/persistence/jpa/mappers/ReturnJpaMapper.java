package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.OrderItemEmbeddable;
import application.adapters.persistence.jpa.entities.ReturnJpaEntity;
import application.domain.models.Order;
import application.domain.models.OrderItem;
import application.domain.models.PhysicalProduct;
import application.domain.models.Return;
import application.domain.valueObjects.ReturnStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnJpaMapper {

    public static ReturnJpaEntity toEntity(Return domain) {
        if (domain == null) return null;
        String orderId = domain.getOrder() != null ? domain.getOrder().getIdentifier() : null;

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

        return ReturnJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .orderIdentifier(orderId)
                .reason(domain.getReason())
                .returnStatus(domain.getReturnStatus() != null ? domain.getReturnStatus().name() : ReturnStatus.REQUESTED.name())
                .requestDate(domain.getRequestDate())
                .items(itemEntities)
                .build();
    }

    public static Return toDomain(ReturnJpaEntity entity) {
        if (entity == null) return null;
        Return returnRequest = new Return();
        returnRequest.setIdentifier(entity.getIdentifier());
        returnRequest.setReason(entity.getReason());
        returnRequest.setRequestDate(entity.getRequestDate());

        if (entity.getReturnStatus() != null) {
            try {
                returnRequest.setReturnStatus(ReturnStatus.valueOf(entity.getReturnStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getOrderIdentifier() != null) {
            Order order = new Order();
            order.setIdentifier(entity.getOrderIdentifier());
            returnRequest.setOrder(order);
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
        returnRequest.setItems(items);
        return returnRequest;
    }
}
