package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.OperationJpaEntity;
import application.domain.models.Administrator;
import application.domain.models.Operation;
import application.domain.models.Order;
import application.domain.valueObjects.OperationType;

public class OperationJpaMapper {

    public static OperationJpaEntity toEntity(Operation domain) {
        if (domain == null) return null;
        String userId = domain.getPerformedBy() != null ? domain.getPerformedBy().getIdentifier() : null;
        String processId = domain.getAffectedProcess() != null ? domain.getAffectedProcess().getIdentifier() : null;

        return OperationJpaEntity.builder()
                .operationId(domain.getOperationId())
                .operationType(domain.getOperationType() != null ? domain.getOperationType().name() : OperationType.ORDER_CREATED.name())
                .executionDate(domain.getExecutionDate())
                .performedByIdentifier(userId)
                .affectedProcessIdentifier(processId)
                .build();
    }

    public static Operation toDomain(OperationJpaEntity entity) {
        if (entity == null) return null;
        Operation operation = new Operation();
        operation.setOperationId(entity.getOperationId());
        operation.setExecutionDate(entity.getExecutionDate());

        if (entity.getOperationType() != null) {
            try {
                operation.setOperationType(OperationType.valueOf(entity.getOperationType()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getPerformedByIdentifier() != null) {
            Administrator user = new Administrator();
            user.setIdentifier(entity.getPerformedByIdentifier());
            operation.setPerformedBy(user);
        }
        if (entity.getAffectedProcessIdentifier() != null) {
            Order process = new Order();
            process.setIdentifier(entity.getAffectedProcessIdentifier());
            operation.setAffectedProcess(process);
        }
        return operation;
    }
}
