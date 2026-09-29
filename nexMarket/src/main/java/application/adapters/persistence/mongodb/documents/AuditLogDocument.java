package application.adapters.persistence.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDocument {

    @Id
    private String auditId;
    private String operationType;
    private LocalDateTime operationDate;
    private String performedByIdentifier;
    private String userRole;
    private String affectedProcessIdentifier;
    private Map<String, Object> details;
}
