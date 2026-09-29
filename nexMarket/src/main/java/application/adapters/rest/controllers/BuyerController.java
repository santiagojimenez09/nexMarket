package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.AddItemToCartRequestDTO;
import application.adapters.rest.dtos.requests.RequestReturnRequestDTO;
import application.adapters.rest.dtos.requests.UpdateAddressesRequestDTO;
import application.adapters.rest.dtos.responses.BuyerResponseDTO;
import application.adapters.rest.dtos.responses.CartResponseDTO;
import application.adapters.rest.dtos.responses.OrderResponseDTO;
import application.adapters.rest.dtos.responses.ReturnResponseDTO;
import application.adapters.rest.mappers.BuyerRestMapper;
import application.adapters.rest.mappers.CartRestMapper;
import application.adapters.rest.mappers.OrderRestMapper;
import application.adapters.rest.mappers.ReturnRestMapper;
import application.domain.models.*;
import application.domain.ports.in.BuyerPort;
import application.infrastructure.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerPort buyerPort;

    @GetMapping("/profile")
    public ResponseEntity<BuyerResponseDTO> getProfile(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        Buyer buyer = buyerPort.consultMyProfile(principal.getUser());
        return ResponseEntity.ok(BuyerRestMapper.toResponseDTO(buyer));
    }

    @PutMapping("/addresses")
    public ResponseEntity<Void> updateAddresses(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                @RequestBody UpdateAddressesRequestDTO requestDTO) {
        buyerPort.updateMyAddresses(principal.getUser(), requestDTO.getPrimaryAddress(), requestDTO.getAdditionalAddresses());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart")
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        ShoppingCart cart = buyerPort.consultMyCart(principal.getUser());
        return ResponseEntity.ok(CartRestMapper.toResponseDTO(cart));
    }

    @PostMapping("/cart/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                         @RequestBody AddItemToCartRequestDTO requestDTO) {
        Product product = new PhysicalProduct();
        product.setIdentifier(requestDTO.getProductIdentifier());
        ShoppingCart cart = buyerPort.addItemToCart(principal.getUser(), product, requestDTO.getQuantity());
        return ResponseEntity.ok(CartRestMapper.toResponseDTO(cart));
    }

    @PutMapping("/cart/items/{productIdentifier}")
    public ResponseEntity<CartResponseDTO> updateCartItem(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                          @PathVariable String productIdentifier,
                                                          @RequestParam int quantity) {
        Product product = new PhysicalProduct();
        product.setIdentifier(productIdentifier);
        ShoppingCart cart = buyerPort.updateCartItem(principal.getUser(), product, quantity);
        return ResponseEntity.ok(CartRestMapper.toResponseDTO(cart));
    }

    @DeleteMapping("/cart/items/{productIdentifier}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                             @PathVariable String productIdentifier) {
        Product product = new PhysicalProduct();
        product.setIdentifier(productIdentifier);
        ShoppingCart cart = buyerPort.removeItemFromCart(principal.getUser(), product);
        return ResponseEntity.ok(CartRestMapper.toResponseDTO(cart));
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        buyerPort.clearCart(principal.getUser());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        Order order = buyerPort.checkoutCart(principal.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderRestMapper.toResponseDTO(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Order> orders = buyerPort.consultMyOrders(principal.getUser());
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{identifier}")
    public ResponseEntity<OrderResponseDTO> getOrder(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                     @PathVariable String identifier) {
        Order query = new Order();
        query.setIdentifier(identifier);
        Order order = buyerPort.consultOrder(principal.getUser(), query);
        return ResponseEntity.ok(OrderRestMapper.toResponseDTO(order));
    }

    @PostMapping("/returns")
    public ResponseEntity<ReturnResponseDTO> requestReturn(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                           @RequestBody RequestReturnRequestDTO requestDTO) {
        Order order = new Order();
        order.setIdentifier(requestDTO.getOrderIdentifier());

        List<OrderItem> items = requestDTO.getItems() == null ? Collections.emptyList() :
                requestDTO.getItems().stream().map(i -> {
                    OrderItem item = new OrderItem();
                    Product p = new PhysicalProduct();
                    p.setIdentifier(i.getProductIdentifier());
                    item.setProduct(p);
                    item.setQuantity(i.getQuantity());
                    return item;
                }).collect(Collectors.toList());

        Return returnReq = buyerPort.requestReturn(principal.getUser(), order, items, requestDTO.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReturnRestMapper.toResponseDTO(returnReq));
    }

    @GetMapping("/returns/{identifier}")
    public ResponseEntity<ReturnResponseDTO> getReturn(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                       @PathVariable String identifier) {
        Return query = new Return();
        query.setIdentifier(identifier);
        Return returnReq = buyerPort.consultReturn(principal.getUser(), query);
        return ResponseEntity.ok(ReturnRestMapper.toResponseDTO(returnReq));
    }
}
