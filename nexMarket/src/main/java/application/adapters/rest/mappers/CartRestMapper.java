package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.CartItemResponseDTO;
import application.adapters.rest.dtos.responses.CartResponseDTO;
import application.domain.models.CartItem;
import application.domain.models.ShoppingCart;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CartRestMapper {

    public static CartResponseDTO toResponseDTO(ShoppingCart cart) {
        if (cart == null) return null;

        List<CartItemResponseDTO> items = cart.getItems() == null ? Collections.emptyList() :
                cart.getItems().stream()
                        .map(CartRestMapper::toItemResponseDTO)
                        .collect(Collectors.toList());

        return CartResponseDTO.builder()
                .buyerIdentifier(cart.getBuyer() != null ? cart.getBuyer().getIdentifier() : null)
                .lastUpdated(cart.getLastUpdated())
                .items(items)
                .build();
    }

    public static CartItemResponseDTO toItemResponseDTO(CartItem item) {
        if (item == null) return null;
        return CartItemResponseDTO.builder()
                .productIdentifier(item.getProduct() != null ? item.getProduct().getIdentifier() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .quantity(item.getQuantity())
                .unitPrice(BigDecimal.valueOf(100.0))
                .build();
    }
}
