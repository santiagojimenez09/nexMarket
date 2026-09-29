package application.adapters.rest.controllers;

import application.adapters.rest.dtos.requests.ChangeStatusRequestDTO;
import application.adapters.rest.dtos.requests.ProcessRefundRequestDTO;
import application.adapters.rest.dtos.requests.RegisterSellerRequestDTO;
import application.adapters.rest.dtos.responses.RefundResponseDTO;
import application.adapters.rest.dtos.responses.ReturnResponseDTO;
import application.adapters.rest.dtos.responses.SellerResponseDTO;
import application.adapters.rest.dtos.responses.WarehouseResponseDTO;
import application.adapters.rest.mappers.ReturnRestMapper;
import application.adapters.rest.mappers.SellerRestMapper;
import application.domain.models.*;
import application.domain.ports.in.AdministratorPort;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserStatus;
import application.domain.valueObjects.WarehouseStatus;
import application.infrastructure.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorPort administratorPort;

    @PostMapping("/sellers")
    public ResponseEntity<SellerResponseDTO> registerSeller(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                            @RequestBody RegisterSellerRequestDTO requestDTO) {
        Seller seller = SellerRestMapper.toDomain(requestDTO);
        Seller saved = administratorPort.registerSeller(principal.getUser(), seller,
                requestDTO.getInitialWarehouseName(), requestDTO.getInitialWarehouseAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(SellerRestMapper.toResponseDTO(saved));
    }

    @PatchMapping("/sellers/{identifier}/status")
    public ResponseEntity<Void> changeSellerStatus(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                   @PathVariable String identifier,
                                                   @RequestBody ChangeStatusRequestDTO requestDTO) {
        Seller seller = new Seller();
        seller.setIdentifier(identifier);
        administratorPort.changeSellerStatus(principal.getUser(), seller, UserStatus.valueOf(requestDTO.getStatus()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerResponseDTO>> listSellers(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Seller> sellers = administratorPort.listSellers(principal.getUser());
        List<SellerResponseDTO> response = sellers.stream()
                .map(SellerRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/buyers/{identifier}/commercial-status")
    public ResponseEntity<Void> changeBuyerCommercialStatus(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                            @PathVariable String identifier,
                                                            @RequestBody ChangeStatusRequestDTO requestDTO) {
        Buyer buyer = new Buyer();
        buyer.setIdentifier(identifier);
        administratorPort.changeBuyerCommercialStatus(principal.getUser(), buyer, BuyerCommercialStatus.valueOf(requestDTO.getStatus()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseResponseDTO>> listWarehouses(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Warehouse> warehouses = administratorPort.listWarehouses(principal.getUser());
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

    @PatchMapping("/returns/{identifier}/approve")
    public ResponseEntity<ReturnResponseDTO> approveReturn(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                           @PathVariable String identifier) {
        Return returnReq = new Return();
        returnReq.setIdentifier(identifier);
        Return approved = administratorPort.approveReturn(principal.getUser(), returnReq);
        return ResponseEntity.ok(ReturnRestMapper.toResponseDTO(approved));
    }

    @PatchMapping("/returns/{identifier}/reject")
    public ResponseEntity<ReturnResponseDTO> rejectReturn(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                          @PathVariable String identifier,
                                                          @RequestParam String reason) {
        Return returnReq = new Return();
        returnReq.setIdentifier(identifier);
        Return rejected = administratorPort.rejectReturn(principal.getUser(), returnReq, reason);
        return ResponseEntity.ok(ReturnRestMapper.toResponseDTO(rejected));
    }

    @PostMapping("/refunds/process")
    public ResponseEntity<RefundResponseDTO> processRefund(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                           @RequestBody ProcessRefundRequestDTO requestDTO) {
        Return returnReq = new Return();
        returnReq.setIdentifier(requestDTO.getReturnIdentifier());
        Refund refund = administratorPort.processRefund(principal.getUser(), returnReq, requestDTO.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReturnRestMapper.toRefundResponseDTO(refund));
    }

    @PostMapping("/refunds/{identifier}/reject")
    public ResponseEntity<RefundResponseDTO> rejectRefund(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                          @PathVariable String identifier,
                                                          @RequestParam String reason) {
        Refund refund = new Refund();
        refund.setIdentifier(identifier);
        Refund rejected = administratorPort.rejectRefund(principal.getUser(), refund, reason);
        return ResponseEntity.ok(ReturnRestMapper.toRefundResponseDTO(rejected));
    }
}
