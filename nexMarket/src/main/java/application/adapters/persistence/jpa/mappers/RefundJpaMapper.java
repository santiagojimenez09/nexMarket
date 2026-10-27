package application.adapters.persistence.jpa.mappers;

import application.adapters.persistence.jpa.entities.RefundJpaEntity;
import application.domain.models.Administrator;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.valueObjects.RefundStatus;

public class RefundJpaMapper {

    public static RefundJpaEntity toEntity(Refund domain) {
        if (domain == null) return null;
        String returnId = domain.getRelatedReturn() != null ? domain.getRelatedReturn().getIdentifier() : null;
        String processedBy = domain.getProcessedBy() != null ? domain.getProcessedBy().getIdentifier() : null;

        return RefundJpaEntity.builder()
                .identifier(domain.getIdentifier())
                .returnIdentifier(returnId)
                .amount(domain.getAmount())
                .refundStatus(domain.getRefundStatus() != null ? domain.getRefundStatus().name() : RefundStatus.PENDING.name())
                .processedByIdentifier(processedBy)
                .build();
    }

    public static Refund toDomain(RefundJpaEntity entity) {
        if (entity == null) return null;
        Refund refund = new Refund();
        refund.setIdentifier(entity.getIdentifier());
        refund.setAmount(entity.getAmount());

        if (entity.getRefundStatus() != null) {
            try {
                refund.setRefundStatus(RefundStatus.valueOf(entity.getRefundStatus()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        if (entity.getReturnIdentifier() != null) {
            Return ret = new Return();
            ret.setIdentifier(entity.getReturnIdentifier());
            refund.setRelatedReturn(ret);
        }
        if (entity.getProcessedByIdentifier() != null) {
            Administrator admin = new Administrator();
            admin.setIdentifier(entity.getProcessedByIdentifier());
            refund.setProcessedBy(admin);
        }
        return refund;
    }
}
