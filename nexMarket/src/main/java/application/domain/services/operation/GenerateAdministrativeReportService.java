package application.domain.services.operation;

import application.domain.models.Operation;
import application.domain.models.Supervisor;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.OperationRepositoryPort;
import application.domain.valueObjects.OperationType;
import application.domain.valueObjects.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateAdministrativeReportService {

    private final OperationRepositoryPort operationRepositoryPort;

    public GenerateAdministrativeReportService(OperationRepositoryPort operationRepositoryPort) {
        this.operationRepositoryPort = operationRepositoryPort;
    }

    public Map<String, Object> generateSummaryReport(Supervisor supervisor) {
        if (supervisor == null || supervisor.getRole() != UserRole.SUPERVISOR) {
            throw new UnauthorizedOperationException("Solo un Supervisor o Administrador puede generar reportes administrativos (OBJ-12).");
        }

        Map<String, Object> report = new HashMap<>();

        Operation orderCreatedQuery = new Operation();
        orderCreatedQuery.setOperationType(OperationType.ORDER_CREATED);
        List<Operation> createdOrders = operationRepositoryPort.findByType(orderCreatedQuery);

        Operation dispatchedQuery = new Operation();
        dispatchedQuery.setOperationType(OperationType.ORDER_DISPATCHED);
        List<Operation> dispatchedOrders = operationRepositoryPort.findByType(dispatchedQuery);

        Operation returnQuery = new Operation();
        returnQuery.setOperationType(OperationType.RETURN_REQUESTED);
        List<Operation> returnRequests = operationRepositoryPort.findByType(returnQuery);

        report.put("totalOrdersCreated", createdOrders.size());
        report.put("totalOrdersDispatched", dispatchedOrders.size());
        report.put("totalReturnsRequested", returnRequests.size());
        report.put("generatedBy", supervisor.getIdentifier());

        return report;
    }
}
