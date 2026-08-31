package application.domains;

import application.valueObjects.OperationType;
import application.valueObjects.UserRole;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuditLog {
    private String auditId;
    private OperationType operationType;
    private LocalDateTime operationDate;
    private User performedBy;
    private UserRole userRole;
    private TrackableProcess affectedProcess;
    private Map<String, Object> details;
}
