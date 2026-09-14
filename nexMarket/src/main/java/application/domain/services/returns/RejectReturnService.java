package application.domain.services.returns;

import application.domain.models.Return;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.ReturnStatus;

public class RejectReturnService {

    private final ReturnRepositoryPort returnRepositoryPort;

    public RejectReturnService(ReturnRepositoryPort returnRepositoryPort) {
        this.returnRepositoryPort = returnRepositoryPort;
    }

    public Return rejectReturn(Return returnRequest, String rejectionReason) {
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            throw new DomainException("El motivo de rechazo de la devolución es obligatorio.");
        }

        Return existing = returnRepositoryPort.findByIdentifier(returnRequest)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de devolución no encontrada: " + returnRequest.getIdentifier()));

        if (existing.getReturnStatus() != ReturnStatus.REQUESTED) {
            throw new InvalidStatusTransitionException("Solo devoluciones en estado SOLICITADA pueden ser rechazadas.");
        }

        existing.setReturnStatus(ReturnStatus.REJECTED);
        returnRepositoryPort.update(existing);
        return existing;
    }
}
