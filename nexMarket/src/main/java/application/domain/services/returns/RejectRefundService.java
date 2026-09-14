package application.domain.services.returns;

import application.domain.models.Administrator;
import application.domain.models.Refund;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.RefundRepositoryPort;
import application.domain.valueObjects.RefundStatus;
import application.domain.valueObjects.UserRole;

public class RejectRefundService {

    private final RefundRepositoryPort refundRepositoryPort;

    public RejectRefundService(RefundRepositoryPort refundRepositoryPort) {
        this.refundRepositoryPort = refundRepositoryPort;
    }

    public Refund rejectRefund(Refund refund, Administrator administrator, String reason) {
        if (administrator == null || administrator.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedOperationException("Solo un Administrador puede rechazar reembolsos.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new DomainException("El motivo de rechazo del reembolso es obligatorio.");
        }

        Refund existingRefund = refundRepositoryPort.save(refund);
        existingRefund.setRefundStatus(RefundStatus.REJECTED);
        refundRepositoryPort.update(existingRefund);

        return existingRefund;
    }
}
