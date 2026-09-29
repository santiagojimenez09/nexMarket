package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.RegisterProductRequestDTO;
import application.adapters.rest.dtos.requests.UpdateProductRequestDTO;
import application.adapters.rest.dtos.responses.InventoryResponseDTO;
import application.adapters.rest.dtos.responses.ProductResponseDTO;
import application.adapters.rest.dtos.responses.SellerResponseDTO;
import application.adapters.rest.dtos.responses.WarehouseResponseDTO;
import application.adapters.rest.mappers.LogisticsRestMapper;
import application.adapters.rest.mappers.ProductRestMapper;
import application.adapters.rest.mappers.SellerRestMapper;
import application.domain.models.*;
import application.domain.ports.in.SellerPort;
import application.infrastructure.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerPort sellerPort;

    @GetMapping("/profile")
    public ResponseEntity<SellerResponseDTO> getProfile(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        Seller seller = sellerPort.consultMyProfile(principal.getUser());
        return ResponseEntity.ok(SellerRestMapper.toResponseDTO(seller));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> registerProduct(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                              @RequestBody RegisterProductRequestDTO requestDTO) {
        Product product = ProductRestMapper.toDomain(requestDTO);
        Product saved = sellerPort.registerProduct(principal.getUser(), product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductRestMapper.toResponseDTO(saved));
    }

    @PutMapping("/products/{identifier}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                            @PathVariable String identifier,
                                                            @RequestBody UpdateProductRequestDTO requestDTO) {
        Product product = new PhysicalProduct();
        product.setIdentifier(identifier);
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        Product updated = sellerPort.updateProduct(principal.getUser(), product);
        return ResponseEntity.ok(ProductRestMapper.toResponseDTO(updated));
    }

    @PatchMapping("/products/{identifier}/publish")
    public ResponseEntity<Void> publishProduct(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                               @PathVariable String identifier) {
        Product product = new PhysicalProduct();
        product.setIdentifier(identifier);
        sellerPort.publishProduct(principal.getUser(), product);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/products/{identifier}/suspend")
    public ResponseEntity<Void> suspendProduct(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                               @PathVariable String identifier) {
        Product product = new PhysicalProduct();
        product.setIdentifier(identifier);
        sellerPort.suspendProduct(principal.getUser(), product);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/products/{identifier}/discontinue")
    public ResponseEntity<Void> discontinueProduct(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                   @PathVariable String identifier) {
        Product product = new PhysicalProduct();
        product.setIdentifier(identifier);
        sellerPort.discontinueProduct(principal.getUser(), product);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getMyProducts(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Product> products = sellerPort.consultMyProducts(principal.getUser());
        List<ProductResponseDTO> response = products.stream()
                .map(ProductRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseResponseDTO>> getMyWarehouses(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Warehouse> warehouses = sellerPort.consultMyWarehouses(principal.getUser());
        List<WarehouseResponseDTO> response = warehouses.stream()
                .map(w -> WarehouseResponseDTO.builder()
                        .identifier(w.getIdentifier())
                        .name(w.getName())
                        .address(w.getAddress())
                        .type(w.getType() != null ? w.getType().name() : null)
                        .status(w.getStatus() != null ? w.getStatus().name() : null)
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouses/{warehouseIdentifier}/inventory")
    public ResponseEntity<List<InventoryResponseDTO>> getMyInventory(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                                     @PathVariable String warehouseIdentifier) {
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier(warehouseIdentifier);
        List<Inventory> inventoryList = sellerPort.consultMyInventory(principal.getUser(), warehouse);
        List<InventoryResponseDTO> response = inventoryList.stream()
                .map(LogisticsRestMapper::toInventoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
