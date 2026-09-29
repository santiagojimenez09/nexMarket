package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.InboundStockRequestDTO;
import application.adapters.rest.dtos.requests.InventoryAdjustmentRequestDTO;
import application.adapters.rest.dtos.requests.PrepareShipmentRequestDTO;
import application.adapters.rest.dtos.responses.InventoryResponseDTO;
import application.adapters.rest.dtos.responses.ShipmentResponseDTO;
import application.adapters.rest.dtos.responses.WarehouseResponseDTO;
import application.adapters.rest.mappers.LogisticsRestMapper;
import application.domain.models.*;
import application.domain.ports.in.LogisticsOperatorPort;
import application.infrastructure.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
public class LogisticsOperatorController {

    private final LogisticsOperatorPort logisticsOperatorPort;

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseResponseDTO>> getOperatedWarehouses(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Warehouse> list = logisticsOperatorPort.consultOperatedWarehouses(principal.getUser());
        List<WarehouseResponseDTO> response = list.stream()
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

    @GetMapping("/inventory")
    public ResponseEntity<InventoryResponseDTO> getInventory(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                             @RequestParam String productIdentifier,
                                                             @RequestParam String warehouseIdentifier) {
        Inventory query = new Inventory();
        PhysicalProduct p = new PhysicalProduct();
        p.setIdentifier(productIdentifier);
        Warehouse w = new Warehouse();
        w.setIdentifier(warehouseIdentifier);
        query.setProduct(p);
        query.setWarehouse(w);

        Inventory inv = logisticsOperatorPort.consultWarehouseInventory(principal.getUser(), query);
        return ResponseEntity.ok(LogisticsRestMapper.toInventoryResponseDTO(inv));
    }

    @PostMapping("/inventory/inbound")
    public ResponseEntity<Void> registerInbound(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                @RequestBody InboundStockRequestDTO requestDTO) {
        Inventory query = new Inventory();
        PhysicalProduct p = new PhysicalProduct();
        p.setIdentifier(requestDTO.getProductIdentifier());
        Warehouse w = new Warehouse();
        w.setIdentifier(requestDTO.getWarehouseIdentifier());
        query.setProduct(p);
        query.setWarehouse(w);

        logisticsOperatorPort.registerInboundStock(principal.getUser(), query, requestDTO.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/inventory/adjustment")
    public ResponseEntity<Void> registerAdjustment(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                   @RequestBody InventoryAdjustmentRequestDTO requestDTO) {
        Inventory query = new Inventory();
        PhysicalProduct p = new PhysicalProduct();
        p.setIdentifier(requestDTO.getProductIdentifier());
        Warehouse w = new Warehouse();
        w.setIdentifier(requestDTO.getWarehouseIdentifier());
        query.setProduct(p);
        query.setWarehouse(w);

        logisticsOperatorPort.registerInventoryAdjustment(principal.getUser(), query, requestDTO.getDeltaQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shipments/prepare")
    public ResponseEntity<ShipmentResponseDTO> prepareShipment(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                               @RequestBody PrepareShipmentRequestDTO requestDTO) {
        Order order = new Order();
        order.setIdentifier(requestDTO.getOrderIdentifier());
        Warehouse warehouse = new Warehouse();
        warehouse.setIdentifier(requestDTO.getWarehouseIdentifier());

        Shipment shipment = logisticsOperatorPort.prepareShipment(principal.getUser(), order, warehouse, requestDTO.getDestinationAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(LogisticsRestMapper.toResponseDTO(shipment));
    }

    @PatchMapping("/shipments/{identifier}/dispatch")
    public ResponseEntity<ShipmentResponseDTO> dispatchShipment(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                                @PathVariable String identifier) {
        Shipment shipment = new Shipment();
        shipment.setIdentifier(identifier);
        Shipment updated = logisticsOperatorPort.dispatchShipment(principal.getUser(), shipment);
        return ResponseEntity.ok(LogisticsRestMapper.toResponseDTO(updated));
    }

    @PatchMapping("/shipments/{identifier}/deliver")
    public ResponseEntity<ShipmentResponseDTO> deliverShipment(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                               @PathVariable String identifier) {
        Shipment shipment = new Shipment();
        shipment.setIdentifier(identifier);
        Shipment updated = logisticsOperatorPort.confirmDelivery(principal.getUser(), shipment);
        return ResponseEntity.ok(LogisticsRestMapper.toResponseDTO(updated));
    }

    @GetMapping("/shipments/assigned")
    public ResponseEntity<List<ShipmentResponseDTO>> getAssignedShipments(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Shipment> shipments = logisticsOperatorPort.consultAssignedShipments(principal.getUser());
        List<ShipmentResponseDTO> response = shipments.stream()
                .map(LogisticsRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
