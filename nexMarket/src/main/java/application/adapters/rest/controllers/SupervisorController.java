package application.adapters.rest.controllers;

import application.adapters.rest.dtos.responses.AuditLogResponseDTO;
import application.adapters.rest.dtos.responses.OrderResponseDTO;
import application.adapters.rest.mappers.AuditLogRestMapper;
import application.adapters.rest.mappers.OrderRestMapper;
import application.domain.models.AuditLog;
import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.models.User;
import application.domain.ports.in.SupervisorPort;
import application.infrastructure.security.AuthenticatedUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/supervisor")
@RequiredArgsConstructor
public class SupervisorController {

    private final SupervisorPort supervisorPort;

    @GetMapping("/reports/summary")
    public ResponseEntity<Map<String, Object>> getSummaryReport(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        Map<String, Object> report = supervisorPort.generateOperationalSummaryReport(principal.getUser());
        return ResponseEntity.ok(report);
    }

    @GetMapping("/audit/user/{identifier}")
    public ResponseEntity<List<AuditLogResponseDTO>> getAuditByUser(@AuthenticationPrincipal AuthenticatedUserPrincipal principal,
                                                                   @PathVariable String identifier) {
        User target = new Buyer();
        target.setIdentifier(identifier);
        List<AuditLog> logs = supervisorPort.consultAuditTrailByUser(principal.getUser(), target);
        List<AuditLogResponseDTO> response = logs.stream()
                .map(AuditLogRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(@AuthenticationPrincipal AuthenticatedUserPrincipal principal) {
        List<Order> orders = supervisorPort.consultAllOrders(principal.getUser());
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderRestMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
