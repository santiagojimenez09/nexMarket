package application.domain.ports.in;

import application.domain.models.Buyer;

public interface RegisterBuyerUseCase {
    Buyer registerBuyer(Buyer buyer, String rawPassword);
}
