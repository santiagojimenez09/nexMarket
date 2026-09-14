package application.domain.services.seller;

import application.domain.models.Seller;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.InvalidStatusTransitionException;
import application.domain.ports.out.SellerRepositoryPort;
import application.domain.valueObjects.UserStatus;

public class ChangeSellerStatusService {

    private final SellerRepositoryPort sellerRepositoryPort;

    public ChangeSellerStatusService(SellerRepositoryPort sellerRepositoryPort) {
        this.sellerRepositoryPort = sellerRepositoryPort;
    }

    public void changeStatus(Seller seller, UserStatus newStatus) {
        if (newStatus == null) {
            throw new InvalidStatusTransitionException("El nuevo estado no puede ser nulo.");
        }

        Seller existingSeller = sellerRepositoryPort.findByIdentifier(seller)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + seller.getIdentifier()));

        existingSeller.setStatus(newStatus);
        sellerRepositoryPort.update(existingSeller);
    }
}
