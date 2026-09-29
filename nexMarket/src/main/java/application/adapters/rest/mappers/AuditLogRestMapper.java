package application.adapters.rest.mappers;

import application.adapters.rest.dtos.responses.AuditLogResponseDTO;
import application.domain.models.AuditLog;

public class AuditLogRestMapper {

    public static AuditLogResponseDTO toResponseDTO(AuditLog log) {
        if (log == null) return null;
        return AuditLogResponseDTO.builder()
                .auditId(log.getAuditId())
                .operationType(log.getOperationType() != null ? log.getOperationType().name() : null)
                .operationDate(log.getOperationDate())
                .performedByIdentifier(log.getPerformedBy() != null ? log.getPerformedBy().getIdentifier() : null)
                .userRole(log.getUserRole() != null ? log.getUserRole().name() : null)
                .details(log.getDetails())
                .build();
    }
}
