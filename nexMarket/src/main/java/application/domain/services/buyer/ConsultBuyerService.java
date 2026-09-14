package application.domain.services.buyer;

import application.domain.models.Buyer;
import application.domain.models.User;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.exceptions.UnauthorizedOperationException;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.valueObjects.UserRole;

public class ConsultBuyerService {

    private final BuyerRepositoryPort buyerRepositoryPort;

    public ConsultBuyerService(BuyerRepositoryPort buyerRepositoryPort) {
        this.buyerRepositoryPort = buyerRepositoryPort;
    }

    public Buyer consultBuyer(Buyer targetBuyer, User requestingUser) {
        if (requestingUser.getRole() == UserRole.BUYER &&
                !requestingUser.getIdentifier().equals(targetBuyer.getIdentifier())) {
            throw new UnauthorizedOperationException("Un comprador no puede acceder a la información de otros compradores (DOMINIO 2).");
        }

        return buyerRepositoryPort.findByIdentifier(targetBuyer)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado: " + targetBuyer.getIdentifier()));
    }
}
