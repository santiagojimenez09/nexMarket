package application.domain.ports.out;

import application.domain.models.AuditLog;
import application.domain.models.TrackableProcess;
import application.domain.models.User;

import java.util.List;

public interface AuditLogRepositoryPort {
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByUser(User user);
    List<AuditLog> findByProcess(TrackableProcess process);
    List<AuditLog> findByOperationType(AuditLog auditLog);
}
