package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.OrderItemResponseDTO;
import application.adapters.rest.dtos.responses.OrderResponseDTO;
import application.domain.models.Order;
import application.domain.models.OrderItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRestMapper {

    public static OrderResponseDTO toResponseDTO(Order order) {
        if (order == null) return null;

        List<OrderItemResponseDTO> items = order.getItems() == null ? Collections.emptyList() :
                order.getItems().stream()
                        .map(OrderRestMapper::toItemResponseDTO)
                        .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .identifier(order.getIdentifier())
                .buyerIdentifier(order.getBuyer() != null ? order.getBuyer().getIdentifier() : null)
                .orderStatus(order.getOrderStatus() != null ? order.getOrderStatus().name() : null)
                .creationDate(order.getCreationDate())
                .items(items)
                .build();
    }

    public static OrderItemResponseDTO toItemResponseDTO(OrderItem item) {
        if (item == null) return null;
        return OrderItemResponseDTO.builder()
                .productIdentifier(item.getProduct() != null ? item.getProduct().getIdentifier() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build();
    }
}
