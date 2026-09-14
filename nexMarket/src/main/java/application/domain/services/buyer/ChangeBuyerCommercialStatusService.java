package application.domain.services.buyer;

import application.domain.models.Buyer;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.valueObjects.BuyerCommercialStatus;

public class ChangeBuyerCommercialStatusService {

    private final BuyerRepositoryPort buyerRepositoryPort;

    public ChangeBuyerCommercialStatusService(BuyerRepositoryPort buyerRepositoryPort) {
        this.buyerRepositoryPort = buyerRepositoryPort;
    }

    public void changeStatus(Buyer buyer, BuyerCommercialStatus newStatus) {
        if (newStatus == null) {
            throw new InvalidStatusTransitionException("El nuevo estado comercial no puede ser nulo.");
        }

        Buyer existingBuyer = buyerRepositoryPort.findByIdentifier(buyer)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado: " + buyer.getIdentifier()));

        existingBuyer.setCommercialStatus(newStatus);
        buyerRepositoryPort.update(existingBuyer);
    }
}
