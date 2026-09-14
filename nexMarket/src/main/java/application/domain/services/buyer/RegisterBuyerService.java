package application.domain.services.buyer;

import application.domain.models.Buyer;
import application.domain.exceptions.DomainException;
import application.domain.ports.in.RegisterBuyerUseCase;
import application.domain.ports.out.BuyerRepositoryPort;
import application.domain.ports.out.PasswordServicePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueObjects.BuyerCommercialStatus;
import application.domain.valueObjects.UserRole;
import application.domain.valueObjects.UserStatus;

public class RegisterBuyerService implements RegisterBuyerUseCase {

    private final BuyerRepositoryPort buyerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    public RegisterBuyerService(BuyerRepositoryPort buyerRepositoryPort,
                                UserRepositoryPort userRepositoryPort,
                                PasswordServicePort passwordServicePort) {
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordServicePort = passwordServicePort;
    }

    public Buyer registerBuyer(Buyer buyer, String rawPassword) {
        if (buyer == null) {
            throw new DomainException("El comprador no puede ser nulo.");
        }
        if (buyer.getPrimaryAddress() == null || buyer.getPrimaryAddress().trim().isEmpty()) {
            throw new DomainException("La dirección principal de entrega es obligatoria (OBJ-03).");
        }
        if (userRepositoryPort.existsByIdentifier(buyer)) {
            throw new DomainException("Ya existe un usuario con el identificador: " + buyer.getIdentifier());
        }
        if (userRepositoryPort.existsByEmail(buyer)) {
            throw new DomainException("Ya existe un usuario con el email: " + buyer.getEmail());
        }

        buyer.setRole(UserRole.BUYER);
        buyer.setStatus(UserStatus.ACTIVE);
        buyer.setCommercialStatus(BuyerCommercialStatus.ACTIVE);

        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            buyer.setPassword(passwordServicePort.encrypt(rawPassword));
        }

        return buyerRepositoryPort.save(buyer);
    }
}
