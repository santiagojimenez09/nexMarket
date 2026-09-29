package application.adapters.persistence.mongodb.mappers;

import application.adapters.persistence.mongodb.documents.AuditLogDocument;
import application.domain.models.AuditLog;
import application.domain.models.Buyer;
import application.domain.models.Order;
import application.domain.valueObjects.OperationType;
import application.domain.valueObjects.UserRole;

public class AuditLogMongoMapper {

    public static AuditLogDocument toDocument(AuditLog domain) {
        if (domain == null) return null;
        return AuditLogDocument.builder()
                .auditId(domain.getAuditId())
                .operationType(domain.getOperationType() != null ? domain.getOperationType().name() : null)
                .operationDate(domain.getOperationDate())
                .performedByIdentifier(domain.getPerformedBy() != null ? domain.getPerformedBy().getIdentifier() : null)
                .userRole(domain.getUserRole() != null ? domain.getUserRole().name() : null)
                .affectedProcessIdentifier(domain.getAffectedProcess() != null ? domain.getAffectedProcess().getIdentifier() : null)
                .details(domain.getDetails())
                .build();
    }

    public static AuditLog toDomain(AuditLogDocument doc) {
        if (doc == null) return null;
        AuditLog log = new AuditLog();
        log.setAuditId(doc.getAuditId());
        if (doc.getOperationType() != null) {
            log.setOperationType(OperationType.valueOf(doc.getOperationType()));
        }
        log.setOperationDate(doc.getOperationDate());

        if (doc.getPerformedByIdentifier() != null) {
            Buyer user = new Buyer();
            user.setIdentifier(doc.getPerformedByIdentifier());
            log.setPerformedBy(user);
        }

        if (doc.getUserRole() != null) {
            log.setUserRole(UserRole.valueOf(doc.getUserRole()));
        }

        if (doc.getAffectedProcessIdentifier() != null) {
            Order process = new Order();
            process.setIdentifier(doc.getAffectedProcessIdentifier());
            log.setAffectedProcess(process);
        }

        log.setDetails(doc.getDetails());
        return log;
    }
}
