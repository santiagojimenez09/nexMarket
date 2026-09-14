package application.domain.services.seller;

import application.domain.models.Administrator;
import application.domain.models.Seller;
import application.domain.exceptions.EntityNotFoundException;
import application.domain.ports.out.SellerRepositoryPort;

import java.util.List;

public class ConsultSellerService {

    private final SellerRepositoryPort sellerRepositoryPort;

    public ConsultSellerService(SellerRepositoryPort sellerRepositoryPort) {
        this.sellerRepositoryPort = sellerRepositoryPort;
    }

    public Seller getByIdentifier(Seller seller) {
        return sellerRepositoryPort.findByIdentifier(seller)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + seller.getIdentifier()));
    }

    public List<Seller> getByRegisteredBy(Administrator administrator) {
        return sellerRepositoryPort.findByRegisteredBy(administrator);
    }
}
