package application.adapters.rest.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {
    private String auditId;
    private String operationType;
    private LocalDateTime operationDate;
    private String performedByIdentifier;
    private String userRole;
    private Map<String, Object> details;
}
