package application.domain.services.returns;

import application.domain.models.Administrator;
import application.domain.models.Refund;
import application.domain.models.Return;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.in.ProcessRefundUseCase;
import application.domain.ports.out.RefundRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.RefundStatus;
import application.domain.valueObjects.ReturnStatus;
import application.domain.valueObjects.UserRole;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessRefundService implements ProcessRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;
    private final ReturnRepositoryPort returnRepositoryPort;

    public ProcessRefundService(RefundRepositoryPort refundRepositoryPort,
                                ReturnRepositoryPort returnRepositoryPort) {
        this.refundRepositoryPort = refundRepositoryPort;
        this.returnRepositoryPort = returnRepositoryPort;
    }

    public Refund processRefund(Return returnRequest, BigDecimal amount, Administrator administrator) {
        if (administrator == null || administrator.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedOperationException("Solo un Administrador puede procesar reembolsos (OBJ-11).");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("El importe del reembolso debe ser mayor a cero.");
        }

        Return existingReturn = returnRepositoryPort.findByIdentifier(returnRequest)
                .orElseThrow(() -> new EntityNotFoundException("Devolución no encontrada: " + returnRequest.getIdentifier()));

        if (existingReturn.getReturnStatus() != ReturnStatus.APPROVED) {
            throw new InvalidStatusTransitionException("Solo se pueden procesar reembolsos para devoluciones aprobadas.");
        }

        Refund refund = new Refund();
        refund.setIdentifier("REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        refund.setRelatedReturn(existingReturn);
        refund.setAmount(amount);
        refund.setRefundStatus(RefundStatus.PROCESSED);
        refund.setProcessedBy(administrator);

        return refundRepositoryPort.save(refund);
    }
}
