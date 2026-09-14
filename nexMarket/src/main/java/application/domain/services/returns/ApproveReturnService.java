package application.domain.services.returns;

import application.domain.models.Return;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueObjects.ReturnStatus;

public class ApproveReturnService {

    private final ReturnRepositoryPort returnRepositoryPort;

    public ApproveReturnService(ReturnRepositoryPort returnRepositoryPort) {
        this.returnRepositoryPort = returnRepositoryPort;
    }

    public Return approveReturn(Return returnRequest) {
        Return existing = returnRepositoryPort.findByIdentifier(returnRequest)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud de devolución no encontrada: " + returnRequest.getIdentifier()));

        if (existing.getReturnStatus() != ReturnStatus.REQUESTED) {
            throw new InvalidStatusTransitionException("Solo devoluciones en estado SOLICITADA pueden ser aprobadas.");
        }

        existing.setReturnStatus(ReturnStatus.APPROVED);
        returnRepositoryPort.update(existing);
        return existing;
    }
}
