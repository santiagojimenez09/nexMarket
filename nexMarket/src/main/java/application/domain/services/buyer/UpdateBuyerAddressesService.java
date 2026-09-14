package application.domain.services.buyer;

import application.domain.models.Buyer;
import application.domain.exceptions.DomainException;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.BuyerRepositoryPort;

import java.util.List;

public class UpdateBuyerAddressesService {

    private final BuyerRepositoryPort buyerRepositoryPort;

    public UpdateBuyerAddressesService(BuyerRepositoryPort buyerRepositoryPort) {
        this.buyerRepositoryPort = buyerRepositoryPort;
    }

    public void updateAddresses(Buyer buyer, String newPrimaryAddress, List<String> newAdditionalAddresses) {
        Buyer existingBuyer = buyerRepositoryPort.findByIdentifier(buyer)
                .orElseThrow(() -> new EntityNotFoundException("Comprador no encontrado: " + buyer.getIdentifier()));

        if (newPrimaryAddress != null && !newPrimaryAddress.trim().isEmpty()) {
            existingBuyer.setPrimaryAddress(newPrimaryAddress);
        } else if (newPrimaryAddress != null && newPrimaryAddress.trim().isEmpty()) {
            throw new DomainException("La dirección principal no puede estar vacía.");
        }

        if (newAdditionalAddresses != null) {
            existingBuyer.setAdditionalAddresses(newAdditionalAddresses);
        }

        buyerRepositoryPort.update(existingBuyer);
    }
}
