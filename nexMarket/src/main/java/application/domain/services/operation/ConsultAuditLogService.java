package application.domain.services.operation;

import application.domain.models.AuditLog;
import application.domain.models.TrackableProcess;
import application.domain.models.User;
import application.domain.ports.out.AuditLogRepositoryPort;

import java.util.List;

public class ConsultAuditLogService {

    private final AuditLogRepositoryPort auditLogRepositoryPort;

    public ConsultAuditLogService(AuditLogRepositoryPort auditLogRepositoryPort) {
        this.auditLogRepositoryPort = auditLogRepositoryPort;
    }

    public List<AuditLog> getByUser(User user) {
        return auditLogRepositoryPort.findByUser(user);
    }

    public List<AuditLog> getByProcess(TrackableProcess process) {
        return auditLogRepositoryPort.findByProcess(process);
    }

    public List<AuditLog> getByOperationType(AuditLog query) {
        return auditLogRepositoryPort.findByOperationType(query);
    }
}
