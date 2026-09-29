package application.adapters.useCases;

import application.domain.models.AuditLog;
import application.domain.models.Operation;
import application.domain.models.Order;
import application.domain.models.Supervisor;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.in.SupervisorPort;
import application.domain.ports.out.OperationRepositoryPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.services.operation.ConsultAuditLogService;
import application.domain.services.operation.GenerateAdministrativeReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupervisorUseCaseImpl implements SupervisorPort {

    private final GenerateAdministrativeReportService generateAdministrativeReportService;
    private final ConsultAuditLogService consultAuditLogService;
    private final OperationRepositoryPort operationRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;

    private Supervisor toSupervisor(User user) {
        Supervisor supervisor = new Supervisor();
        supervisor.setIdentifier(user.getIdentifier());
        supervisor.setFullName(user.getFullName());
        supervisor.setEmail(user.getEmail());
        supervisor.setRole(user.getRole());
        supervisor.setStatus(user.getStatus());
        return supervisor;
    }

    @Override
    public Map<String, Object> generateOperationalSummaryReport(User supervisor) {
        return generateAdministrativeReportService.generateSummaryReport(toSupervisor(supervisor));
    }

    @Override
    public List<AuditLog> consultAuditTrailByProcess(User supervisor, TrackableProcess process) {
        return consultAuditLogService.getByProcess(process);
    }

    @Override
    public List<AuditLog> consultAuditTrailByUser(User supervisor, User targetUser) {
        return consultAuditLogService.getByUser(targetUser);
    }

    @Override
    public List<Operation> consultOperations(User supervisor, Operation query) {
        return operationRepositoryPort.findByType(query);
    }

    @Override
    public List<Order> consultAllOrders(User supervisor) {
        Order query = new Order();
        return orderRepositoryPort.findByStatus(query);
    }
}
