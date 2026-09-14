package application.domain.services.operation;

import application.domain.models.AuditLog;
import application.domain.models.Operation;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.exceptions.DomainException;
import application.domain.ports.out.AuditLogRepositoryPort;
import application.domain.ports.out.OperationRepositoryPort;
import application.domain.valueObjects.OperationType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class RegisterOperationAndAuditService {

    private final OperationRepositoryPort operationRepositoryPort;
    private final AuditLogRepositoryPort auditLogRepositoryPort;

    public RegisterOperationAndAuditService(OperationRepositoryPort operationRepositoryPort,
                                           AuditLogRepositoryPort auditLogRepositoryPort) {
        this.operationRepositoryPort = operationRepositoryPort;
        this.auditLogRepositoryPort = auditLogRepositoryPort;
    }

    public Operation registerOperationAndAudit(OperationType operationType,
                                              User performedBy,
                                              TrackableProcess affectedProcess,
                                              Map<String, Object> details) {
        if (operationType == null) {
            throw new DomainException("El tipo de operación es obligatorio.");
        }
        if (performedBy == null) {
            throw new DomainException("El usuario que realiza la operación es obligatorio.");
        }
        if (affectedProcess == null) {
            throw new DomainException("El proceso rastreable afectado es obligatorio.");
        }

        LocalDateTime now = LocalDateTime.now();

        // 1. Register business Operation
        Operation operation = new Operation();
        operation.setOperationId("OP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        operation.setOperationType(operationType);
        operation.setExecutionDate(now);
        operation.setPerformedBy(performedBy);
        operation.setAffectedProcess(affectedProcess);
        Operation savedOperation = operationRepositoryPort.save(operation);

        // 2. Register immutable AuditLog (append-only)
        AuditLog auditLog = new AuditLog();
        auditLog.setAuditId("AUD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        auditLog.setOperationType(operationType);
        auditLog.setOperationDate(now);
        auditLog.setPerformedBy(performedBy);
        auditLog.setUserRole(performedBy.getRole());
        auditLog.setAffectedProcess(affectedProcess);
        auditLog.setDetails(details);
        auditLogRepositoryPort.save(auditLog);

        return savedOperation;
    }
}
